package com.syouth.kmapper.processor.convertors.manager

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.base.extractSupportedCollectionTypeArgumentType
import com.syouth.kmapper.processor.base.findUserDefinedMapperMethods
import com.syouth.kmapper.processor.base.isSupportedCollectionType
import com.syouth.kmapper.processor.convertors.TypeConvertor
import com.syouth.kmapper.processor.convertors.UserDefinedMethodConverter
import com.syouth.kmapper.processor.convertors.UserDefinedPropertyConverter
import com.syouth.kmapper.processor_annotations.Bind
import com.syouth.kmapper.processor_annotations.Mapping

internal class ConvertersManagerImpl(
    private val builtInConverters: List<TypeConvertor>
) : ConvertersManager {

    private val mapperClassUserDefinedMethods = mutableListOf<UserDefinedMethodConverter>()
    private val mapperFunctionUserDefinedConvertors = mutableListOf<UserDefinedPropertyConverter>()

    val propertyConverters: List<UserDefinedPropertyConverter> = mapperFunctionUserDefinedConvertors

    override fun findConverterForTypes(from: KSType?, to: KSType, targetPath: PathHolder?): TypeConvertor? {
        // First priority: Mapping annotation
        mapperFunctionUserDefinedConvertors.find { it.isSupported(from, to, targetPath) }?.let { return it }

        // Check if we have something in user defined functions as priority is higher
        mapperClassUserDefinedMethods.find { it.isSupported(from, to, targetPath) }?.let { return it }

        // Then check built in converters
        return builtInConverters.find {
            it.isSupported(from, to, targetPath)
        }
    }

    override fun initializeForMapperClass(mapper: KSClassDeclaration) {
        mapperClassUserDefinedMethods.clear()
        mapper.findUserDefinedMapperMethods().forEach {
            mapperClassUserDefinedMethods += UserDefinedMethodConverter(it.parameters[0].type.resolve(), it.returnType!!.resolve(), it)
        }
    }

    @OptIn(KspExperimental::class)
    override fun initializeForMapperFunction(func: KSFunctionDeclaration) {
        mapperFunctionUserDefinedConvertors.clear()
        func.getAnnotationsByType(Mapping::class).forEach {
            mapperFunctionUserDefinedConvertors += UserDefinedPropertyConverter(
                convertersManager = this,
                targetPath = buildPathHolderForType(it.target, func.returnType?.resolve() ?: throw IllegalStateException("Mapper should have a return type")),
                sourcePath = buildSourcePathHolderFromString(it.source, func)
            )
        }
        for (parameter in func.parameters) {
            parameter.getAnnotationsByType(Bind::class).forEach {
                val paramName = parameter.name ?: throw IllegalStateException("Parameter should have a name to be bindable")
                val target = it.to.ifEmpty { paramName.asString() }
                val source = paramName.asString()
                mapperFunctionUserDefinedConvertors += UserDefinedPropertyConverter(
                    convertersManager = this,
                    targetPath = buildPathHolderForType(target, func.returnType?.resolve() ?: throw IllegalStateException("Mapper should have a return type")),
                    sourcePath = buildSourcePathHolderFromString(source, func)
                )
            }
        }
    }

    private fun buildPathHolderForType(path: String, type: KSType): PathHolder {
        val result = PathHolder()
        val elements = path.split('.')
        var currentType = type
        for (element in elements) {
            if (currentType.isSupportedCollectionType()) {
                currentType = currentType.extractSupportedCollectionTypeArgumentType()
            }
            val typeDeclaration = currentType.declaration
            if (typeDeclaration !is KSClassDeclaration) throw IllegalStateException("Return type should be a class")
            val foundPropertyType = typeDeclaration
                .getAllProperties()
                .find { it.simpleName.asString() == element }?.type?.resolve()
                ?: throw IllegalStateException("Target path should reference valid properties. Can't find $element from $path")
            result.appendPathElement(PathHolder.PathElement(element, foundPropertyType))
            currentType = foundPropertyType
        }

        return result
    }

    private fun buildSourcePathHolderFromString(path: String, func: KSFunctionDeclaration): PathHolder {
        val result = PathHolder()
        val indexOfFirstDot = path.indexOfFirst { it == '.' }
        val firstElement = if (indexOfFirstDot != -1) path.substring(0, indexOfFirstDot) else path
        val currentType = func.parameters.find { it.name?.asString() == firstElement } ?: throw IllegalStateException("Target path should reference valid properties. Can't find $firstElement from $path")
        val resolvedType = currentType.type.resolve()
        result.appendPathElement(PathHolder.PathElement(firstElement, resolvedType))
        if (indexOfFirstDot != -1) {
            val restOfPath = buildPathHolderForType(path.substring(indexOfFirstDot + 1), resolvedType)
            result.appendPath(restOfPath)
        }
        return result
    }
}