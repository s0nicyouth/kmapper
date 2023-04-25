package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.*
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.base.checkDifferentTypesNullabilitySufficient
import com.syouth.kmapper.processor.base.checkMapCollectionTypeArgumentsNullabilitySufficient
import com.syouth.kmapper.processor.base.checkMapCollectionTypeKeyArgumentsNullabilitySufficient
import com.syouth.kmapper.processor.base.isSupportedMapCollectionType
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement
import com.syouth.kmapper.processor.strategies.CheckCycleStrategy
import com.syouth.kmapper.processor.strategies.VisitNodeStrategy

internal class MapCollectionConverter(
    private val convertersManager: ConvertersManager,
    private val checkCycleStrategy: CheckCycleStrategy,
    private val nodeVisitorStrategy: VisitNodeStrategy
) : TypeConvertor {

    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (from == null) return false
        return from.isSupportedMapCollectionType()
                && to.isSupportedMapCollectionType()
                && checkDifferentTypesNullabilitySufficient(from, to)
                && checkMapCollectionTypeKeyArgumentsNullabilitySufficient(from, to)
                && checkMapCollectionTypeArgumentsNullabilitySufficient(from, to)
    }

    override fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType,
        targetPath: PathHolder?,
        bundle: Bundle
    ): AssignableStatement {
        if (from == null || fromParameterSpec == null) throw IllegalStateException("from type or from object name can't be null here")
        checkCycleStrategy(bundle, from)
        return nodeVisitorStrategy.scoped(bundle, from) {
            AssignableStatement(
                code = when {
                    areSameSupportedMapCollectionTypes(from, to) -> buildCodeBlockForSameTypes(fromParameterSpec)
                    from.isSupportedMapCollectionType() && to.isSupportedMapCollectionType() -> buildCodeBlockForDataClasses(
                        fromParameterSpec,
                        from,
                        to,
                        bundle
                    )
                    else -> throw IllegalStateException("One of or both data types not supported: ${from.declaration.simpleName} ${to.declaration.simpleName}")
                },
                requiresObjectToConvertFrom = true
            )
        }
    }

    private fun buildCodeBlockForSameTypes(fromParameterSpec: ParameterSpec): CodeBlock = buildCodeBlock {
        add("%N", fromParameterSpec)
    }

    private fun buildCodeBlockForDataClasses(fromParameterSpec: ParameterSpec, from: KSType, to: KSType, bundle: Bundle): CodeBlock = buildCodeBlock {
        val fromMapCollectionArgumentType = from.extractSupportedMapCollectionTypeArgument()
        val toMapCollectionArgumentType = to.extractSupportedMapCollectionTypeArgument()
        val resultTypeSpec = to.getCorrespondingConcreteTypeForSupportedCollectionType()
        val fromMapCollectionKeyTypeArgument = from.extractSupportedMapCollectionKeyTypeArgument()
        val toMapCollectionKeyTypeArgument = to.extractSupportedMapCollectionKeyTypeArgument()
        beginControlFlow("run {")
        if (from.nullability == Nullability.NULLABLE) {
            beginControlFlow("if·(%N·==·null)·{", fromParameterSpec)
            addStatement("return@run·null")
            endControlFlow()
        }
        addStatement("val·result·=·%T()", resultTypeSpec)
        run {
            beginControlFlow("for·(entry·in·%N)·{", fromParameterSpec)
            val keyConverter = convertersManager.findConverterForTypes(
                fromMapCollectionKeyTypeArgument,
                toMapCollectionKeyTypeArgument,
                null
            ) ?: throw IllegalStateException("Unable to find converter from ${fromMapCollectionKeyTypeArgument.toTypeName()} to ${toMapCollectionKeyTypeArgument.toTypeName()}")
            val nonNullableFromMapCollectionArgumentType = fromMapCollectionArgumentType.makeNotNullable()
            val valueConvertor = convertersManager.findConverterForTypes(
                nonNullableFromMapCollectionArgumentType,
                toMapCollectionArgumentType,
                null
            ) ?: throw IllegalStateException("Unable to find converter from ${nonNullableFromMapCollectionArgumentType.toTypeName()} to ${toMapCollectionArgumentType.toTypeName()}")
            val keyObjParameterSpec = ParameterSpec.builder("it", fromMapCollectionKeyTypeArgument.toClassName()).build()
            val keyConversionStatement = keyConverter.buildConversionStatement(
                keyObjParameterSpec,
                fromMapCollectionKeyTypeArgument,
                toMapCollectionKeyTypeArgument,
                null,
                bundle
            )
            val valueObjParameterSpec = ParameterSpec.builder("it", nonNullableFromMapCollectionArgumentType.toClassName()).build()
            val valueConversionStatement = valueConvertor.buildConversionStatement(
                valueObjParameterSpec,
                nonNullableFromMapCollectionArgumentType,
                toMapCollectionArgumentType,
                null,
                bundle
            )
            addStatement("val·(k,·v)·=·entry")
            if (keyConversionStatement.requiresObjectToConvertFrom) {
                beginControlFlow("val·newK·=·k.let·{")
                add("%L\n", keyConversionStatement.code)
                endControlFlow()
            } else {
                add("val·newK·=·%L\n", keyConversionStatement.code)
            }
            if (fromMapCollectionArgumentType.nullability == Nullability.NULLABLE) {
                beginControlFlow("if·(v·==·null)·{")
                addStatement("result[newK]·=·null")
                addStatement("continue")
                endControlFlow()
            }
            run {
                if (valueConversionStatement.requiresObjectToConvertFrom) {
                    beginControlFlow("val·newV·=·v.let·{")
                    add("%L\n", valueConversionStatement.code)
                    endControlFlow()
                } else {
                    add("val·newV·=·%L\n", valueConversionStatement.code)
                }
            }
            addStatement("result[newK]·=·newV")
            endControlFlow()
        }
        addStatement("result")
        endControlFlow()
    }
}