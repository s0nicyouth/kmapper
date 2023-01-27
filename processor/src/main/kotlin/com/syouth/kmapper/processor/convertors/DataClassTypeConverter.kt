package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.base.buildMappingTable
import com.syouth.kmapper.processor.base.checkDifferentTypesNullabilitySufficient
import com.syouth.kmapper.processor.base.data.MappingProperties
import com.syouth.kmapper.processor.base.isDataClass
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement

internal class DataClassTypeConverter(
    private val convertersManager: ConvertersManager
) : TypeConvertor {
    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (from == null) return false
        return (from.isDataClass() || to.isDataClass()) &&
                checkDifferentTypesNullabilitySufficient(from, to)
    }

    override fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType,
        targetPath: PathHolder?
    ): AssignableStatement = AssignableStatement(
        code = when {
            fromParameterSpec == null || from == null -> throw IllegalStateException("From type or object name can't be null here")
            from == to || from == to.makeNotNullable() -> buildCodeBlockForSameTypes(fromParameterSpec)
            checkDifferentTypesNullabilitySufficient(from, to) -> buildCodeBlockForDifferentTypes(fromParameterSpec, from, to, targetPath)
            else -> throw IllegalStateException("Can not map nullable to non nullable class")
        },
        requiresObjectToConvertFrom = true
    )

    private fun buildCodeBlockForSameTypes(fromObjectName: ParameterSpec): CodeBlock = buildCodeBlock {
        add("%N", fromObjectName)
    }

    private fun buildCodeBlockForDifferentTypes(fromObjectName: ParameterSpec, from: KSType, to: KSType, targetPath: PathHolder?): CodeBlock = buildCodeBlock {
        val mappingPropertiesWithDefaults = (to.declaration as KSClassDeclaration).buildMappingTable(from.declaration as KSClassDeclaration)
        val conversionStatementIndexToMappingProperty = mutableMapOf<Int, MappingProperties>()
        var statementIndex = 0
        val conversionBlocks: List<AssignableStatement> = mappingPropertiesWithDefaults.mapNotNull {
            val additionalPath = it.to.name?.let { name -> PathHolder.PathElement(name.asString(), it.to.type.resolve()) } ?: throw IllegalStateException("to property should have a name")
            targetPath?.appendPathElement(additionalPath)
            val fromType = it.from?.type?.resolve()
            val converter = convertersManager.findConverterForTypes(fromType, it.to.type.resolve(), targetPath)
            if (converter == null && !it.to.hasDefault) throw IllegalStateException("Do not know how to convert ${it.to.type.toTypeName()} with name ${it.to.name?.asString()} and path $targetPath") // No converter and no default value means fail
            // Skip generation for inconvertible value with default
            if (converter == null && it.to.hasDefault) {
                targetPath?.removeLastPathElement()
                return@mapNotNull null
            }
            if (converter == null) throw IllegalStateException("Should not ever happen")
            val paramSpec = ParameterSpec.builder("it", fromType?.toTypeName() ?: Unit::class.asTypeName()).build()
            val conversionStatement = converter.buildConversionStatement(paramSpec, fromType, it.to.type.resolve(), targetPath)
            targetPath?.removeLastPathElement()
            conversionStatementIndexToMappingProperty[statementIndex++] = it
            conversionStatement
        }
        check(conversionStatementIndexToMappingProperty.size == conversionBlocks.size) { "Number of block should be equal to number of mappings" }
        if (from.nullability == Nullability.NULLABLE) {
            beginControlFlow("if·(%N·==·null)·{", fromObjectName)
            addStatement("null")
            nextControlFlow("else")
        }
        add("%T(\n", (to.declaration as KSClassDeclaration).toClassName())
        indent()
        conversionBlocks.withIndex().forEach { (index, statement) ->
            val mappingProperty = conversionStatementIndexToMappingProperty[index] ?: throw IllegalStateException("Can't find mapping information for conversion statement")
            if (statement.requiresObjectToConvertFrom) {
                check(mappingProperty.from != null) { "From property is required and should not be null" }
                beginControlFlow("${mappingProperty.to.name?.asString()}·=·%N.${mappingProperty.from.simpleName.asString()}.let·{", fromObjectName)
                add("%L\n", statement.code)
                endControlFlow()
            } else {
                add("${mappingProperty.to.name?.asString()} = %L\n", statement.code)
            }
            if (index < conversionBlocks.size - 1) add(",\n")
        }
        unindent()
        add(")\n")
        if (from.nullability == Nullability.NULLABLE) {
            endControlFlow()
        }
    }
}
