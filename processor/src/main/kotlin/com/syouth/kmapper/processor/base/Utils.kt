package com.syouth.kmapper.processor.base

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.syouth.kmapper.processor.base.data.MapperInformation
import com.syouth.kmapper.processor.base.data.MappingProperties
import kotlin.reflect.KClass

internal inline fun<reified T> Resolver.findAnnotations(
    kClass: KClass<*>
) = getSymbolsWithAnnotation(kClass.qualifiedName.toString())
    .filterIsInstance<T>()

internal fun KSTypeReference.checkSame(cls: KClass<*>): Boolean =
    resolve().declaration.qualifiedName?.asString() == cls.qualifiedName

internal fun KSClassDeclaration.findAbstractMappingFunctionDeclarations() =
    getAllFunctions()
        .filter {
            it.isAbstract &&
                    it.returnType != null &&
                    it.returnType?.resolve()?.declaration is KSClassDeclaration &&
                    it.parameters.getOrNull(0) != null &&
                    it.parameters[0].type.resolve().declaration is KSClassDeclaration
        }

internal fun KSClassDeclaration.findUserDefinedMapperMethods() =
    getAllFunctions()
        .filter {
            !it.isAbstract &&
                    it.returnType != null &&
                    it.returnType?.resolve()?.declaration is KSClassDeclaration &&
                    it.parameters.getOrNull(0) != null &&
                    it.parameters[0].type.resolve().declaration is KSClassDeclaration
        }

internal fun KSFunctionDeclaration.getMappingInformation(): MapperInformation {
    val from = parameters[0].type.resolve()
    val to = returnType!!.resolve()
    if (from.declaration !is KSClassDeclaration || to.declaration !is KSClassDeclaration) throw IllegalStateException("Mapping should be from class to class.")
    if (!from.isDataClass() || !to.isDataClass()) throw IllegalStateException("Mapping is only supported for data classes")
    return MapperInformation(
        mapperParams = parameters,
        to = to,
        func = this
    )
}

internal fun KSClassDeclaration.buildMappingTable(from: KSClassDeclaration): List<MappingProperties> {
    val result = mutableListOf<MappingProperties>()
    val toProperties = primaryConstructor?.parameters ?: throw IllegalStateException("Data class should have primary constructor")
    val fromProperties = from.getAllProperties().filter { it.isPublic() }
    for (toProp in toProperties) {
        val fromProp = fromProperties.find { it.simpleName.asString() == toProp.name?.asString() }
        result += MappingProperties(
            from = fromProp,
            to = toProp
        )
    }

    return result
}