package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.*
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement
import com.syouth.kmapper.processor.strategies.CheckCycleStrategy
import com.syouth.kmapper.processor.strategies.VisitNodeStrategy

internal class CollectionTypeConverter(
    private val convertersManager: ConvertersManager,
    private val checkCycleStrategy: CheckCycleStrategy,
    private val nodeVisitorStrategy: VisitNodeStrategy
) : TypeConvertor {

    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (from == null) return false
        return from.isSupportedCollectionType() && to.isSupportedCollectionType() &&
                checkDifferentTypesNullabilitySufficient(from, to) &&
                checkCollectionArgumentsNullabilitySufficient(from, to)
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
                    areSameSupportedCollectionTypes(from, to) -> buildCodeBlockForSameTypes(fromParameterSpec)
                    from.isSupportedCollectionType() && to.isSupportedCollectionType() -> buildCodeBlockForDataClasses(
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
        val fromCollectionArgumentType = from.extractSupportedCollectionTypeArgumentType()
        val toCollectionArgumentType = to.extractSupportedCollectionTypeArgumentType()
        val resultTypeSpec = to.getCorrespondingConcreteTypeForSupportedCollectionType()
        beginControlFlow("run {")
        if (from.nullability == Nullability.NULLABLE) {
            beginControlFlow("if·(%N·==·null)·{", fromParameterSpec)
            addStatement("return@run·null")
            endControlFlow()
        }
        addStatement("val·result·=·%T()", resultTypeSpec)
        run {
            beginControlFlow("for·(obj·in·%N)·{", fromParameterSpec)
            val nonNullFromCollectionArgumentType = fromCollectionArgumentType.makeNotNullable()
            val convertor = convertersManager.findConverterForTypes(
                nonNullFromCollectionArgumentType,
                toCollectionArgumentType,
                null
            ) ?: throw IllegalStateException("Unable to find converter from ${nonNullFromCollectionArgumentType.toTypeName()} to ${toCollectionArgumentType.toTypeName()}")
            val objParameterSpec = ParameterSpec.builder("it", nonNullFromCollectionArgumentType.toClassName()).build()
            val conversionStatement = convertor.buildConversionStatement(
                objParameterSpec,
                nonNullFromCollectionArgumentType,
                toCollectionArgumentType,
                null,
                bundle
            )
            if (fromCollectionArgumentType.nullability == Nullability.NULLABLE) {
                beginControlFlow("if·(obj·==·null)·{")
                addStatement("result·+=·null")
                addStatement("continue")
                endControlFlow()
            }
            run {
                if (conversionStatement.requiresObjectToConvertFrom) {
                    beginControlFlow("val·converted·=·obj.let·{")
                    add("%L\n", conversionStatement.code)
                    endControlFlow()
                } else {
                    add("val·converted·=·%L\n", conversionStatement.code)
                }
            }
            addStatement("result·+=·converted")
            endControlFlow()
        }
        addStatement("result")
        endControlFlow()
    }
}