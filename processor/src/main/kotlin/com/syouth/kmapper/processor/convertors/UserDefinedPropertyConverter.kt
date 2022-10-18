package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.base.checkDifferentTypesNullabilitySufficient
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement

internal class UserDefinedPropertyConverter(
    private val convertersManager: ConvertersManager,
    private val targetPath: PathHolder,
    private val sourcePath: PathHolder
    ) : TypeConvertor {
    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (targetPath == null) return false
        return this.targetPath.path.joinToString { it.elementName } == targetPath.path.joinToString { it.elementName } &&
                checkDifferentTypesNullabilitySufficient(sourcePath.getLastElementFinalType(), to)
    }

    override fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType,
        targetPath: PathHolder?
    ): AssignableStatement {
        val sourceFinalType = sourcePath.getLastElementFinalType()
        val converter = convertersManager.findConverterForTypes(sourceFinalType, to, null) ?: throw IllegalStateException("Can't find converter for $targetPath")
        val parameterSpec = ParameterSpec.builder("it", sourceFinalType.toTypeName()).build()
        val conversionStatement = converter.buildConversionStatement(parameterSpec, sourceFinalType, to, targetPath)
        return AssignableStatement(
            code = buildCodeBlock {
                if (conversionStatement.requiresObjectToConvertFrom) {
                    beginControlFlow("${sourcePath.buildAccessStatementConsideringNullability()}.let {")
                    add("%L\n", conversionStatement.code)
                    endControlFlow()
                } else {
                    add("%L\n", conversionStatement.code)
                }
            },
            requiresObjectToConvertFrom = false
        )
    }

    private fun PathHolder.getLastElementFinalType(): KSType =
        if (path.any { it.elementType.nullability != Nullability.NOT_NULL }) path.last().elementType.makeNullable() else path.last().elementType

    private fun PathHolder.buildAccessStatementConsideringNullability(): String {
        val statementBuilder = StringBuilder()
        path.withIndex().forEach { (index, segment) ->
            if (index == 0) {
                statementBuilder.append(segment.elementName)
            } else {
                statementBuilder.append(".")
                statementBuilder.append(segment.elementName)
            }
        }

        return statementBuilder.toString()
    }
}