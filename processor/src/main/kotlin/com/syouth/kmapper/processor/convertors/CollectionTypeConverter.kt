package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.*
import com.syouth.kmapper.processor.base.areSameSupportedCollectionTypes
import com.syouth.kmapper.processor.base.getCorrespondingConcreteTypeForSupportedCollectionType
import com.syouth.kmapper.processor.base.isCollectionTypeArgumentDataClass
import com.syouth.kmapper.processor.base.isSupportedCollectionType
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement

internal class CollectionTypeConverter(
    private val convertersManager: ConvertersManager
) : TypeConvertor {

    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (from == null) return false
        return from.isSupportedCollectionType() && to.isSupportedCollectionType() &&
                checkCollectionArgumentsNullabilitySufficient(from, to)
    }

    override fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType,
        targetPath: PathHolder?
    ): AssignableStatement {
        if (from == null || fromParameterSpec == null) throw IllegalStateException("from type or from object name can't be null here")
        return AssignableStatement(
            code = when {
                areSameSupportedCollectionTypes(from, to) -> buildCodeBlockForSameTypes(fromParameterSpec)
                from.isCollectionTypeArgumentDataClass() && to.isCollectionTypeArgumentDataClass() -> buildCodeBlockForDataClasses(fromParameterSpec, from, to, targetPath)
                else -> throw IllegalStateException("One of or both data types not supported: ${from.declaration.simpleName} ${to.declaration.simpleName}")
            },
            requiresObjectToConvertFrom = true
        )
    }

    private fun buildCodeBlockForSameTypes(fromParameterSpec: ParameterSpec): CodeBlock = buildCodeBlock {
        add("%N", fromParameterSpec)
    }

    private fun buildCodeBlockForDataClasses(fromParameterSpec: ParameterSpec, from: KSType, to: KSType, targetPath: PathHolder?): CodeBlock = buildCodeBlock {
        val fromCollectionArgumentType = from.extractSupportedCollectionTypeArgumentType()
        val toCollectionArgumentType = to.extractSupportedCollectionTypeArgumentType()
        val resultTypeSpec = to.getCorrespondingConcreteTypeForSupportedCollectionType()
        beginControlFlow("run {")
        addStatement("val result = %T()", resultTypeSpec)
        run {
            beginControlFlow("for (obj in %N) {", fromParameterSpec)
            val nonNullFromCollectionArgumentType = fromCollectionArgumentType.makeNotNullable()
            val convertor = convertersManager.findConverterForTypes(
                nonNullFromCollectionArgumentType,
                toCollectionArgumentType,
                targetPath
            ) ?: throw IllegalStateException("Unable to find converter for ${nonNullFromCollectionArgumentType.toTypeName()}")
            val objParameterSpec = ParameterSpec.builder("it", nonNullFromCollectionArgumentType.toClassName()).build()
            val conversionStatement = convertor.buildConversionStatement(
                objParameterSpec,
                nonNullFromCollectionArgumentType,
                toCollectionArgumentType,
                targetPath
            )
            if (fromCollectionArgumentType.nullability == Nullability.NULLABLE) {
                beginControlFlow("if (obj == null) {")
                addStatement("result += null")
                addStatement("continue")
                endControlFlow()
            }
            run {
                if (conversionStatement.requiresObjectToConvertFrom) {
                    beginControlFlow("val converted = obj.let {")
                    add("%L\n", conversionStatement.code)
                    endControlFlow()
                } else {
                    add("val converted = %L", conversionStatement.code)
                }
            }
            addStatement("result += converted")
            endControlFlow()
        }
        addStatement("result")
        endControlFlow()
    }
}