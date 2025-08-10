package com.syouth.kmapper.processor.base

import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.data.SUPPORTED_CONVERSION_INTERFACES
import com.syouth.kmapper.processor.base.data.SUPPORTED_CONVERSION_INTERFACES_CORRESPONDING_CONCRETE_TYPES
import com.syouth.kmapper.processor.base.data.SUPPORTED_MAP_CONVERSION_INTERFACES

internal fun KSType.isDataClass() =
    declaration.modifiers.contains(Modifier.DATA)

internal fun KSType.isSupportedCollectionType() =
    declaration.qualifiedName?.asString() in SUPPORTED_CONVERSION_INTERFACES

internal fun KSType.isSupportedMapCollectionType() =
    declaration.qualifiedName?.asString() in SUPPORTED_MAP_CONVERSION_INTERFACES

internal fun areSameSupportedCollectionTypes(first: KSType, second: KSType): Boolean {
    if (first.declaration.qualifiedName?.asString() != second.declaration.qualifiedName?.asString()) return false
    if (first.declaration.qualifiedName?.asString() !in SUPPORTED_CONVERSION_INTERFACES) return false
    if (first.arguments.size != second.arguments.size) return false
    if (!first.arguments.zip(second.arguments).all { (f, s) -> checkSameTypeWithNullabilitySufficient(f.type?.resolve(), s.type?.resolve()) }) return false
    return true
}

internal fun areSameSupportedMapCollectionTypes(first: KSType, second: KSType): Boolean {
    if (first.declaration.qualifiedName?.asString() != second.declaration.qualifiedName?.asString()) return false
    if (first.declaration.qualifiedName?.asString() !in SUPPORTED_MAP_CONVERSION_INTERFACES) return false
    if (first.arguments.size != second.arguments.size) return false
    return checkMapCollectionTypeKeyArgumentsNullabilitySufficient(first, second)
            && checkSameTypeWithNullabilitySufficient(first.arguments.firstOrNull()?.type?.resolve(), second.arguments.firstOrNull()?.type?.resolve())
}

internal fun KSType.getCorrespondingConcreteTypeForSupportedCollectionType(): TypeName {
    val qualifiedName = declaration.qualifiedName?.asString() ?: throw IllegalStateException("Can't resolve qualified name")
    if (qualifiedName in SUPPORTED_CONVERSION_INTERFACES || qualifiedName in SUPPORTED_MAP_CONVERSION_INTERFACES) {
        val concreteType = SUPPORTED_CONVERSION_INTERFACES_CORRESPONDING_CONCRETE_TYPES[qualifiedName] ?: throw IllegalStateException("Can't find concrete type for collection type")
        val indexOfLastDot = concreteType.lastIndexOf(".")
        if (indexOfLastDot == -1) throw IllegalStateException("Wrong type qualified name")
        val packageName = concreteType.substring(0, indexOfLastDot)
        val typeName = concreteType.substring(indexOfLastDot + 1)
        return if (qualifiedName in SUPPORTED_CONVERSION_INTERFACES) {
            ClassName(packageName, typeName).parameterizedBy(arguments[0].toTypeName())
        } else {
            ClassName(packageName, typeName).parameterizedBy(arguments[0].toTypeName(), arguments[1].toTypeName())
        }
    } else {
        throw IllegalStateException("Collection type not supported")
    }
}

internal fun checkCollectionArgumentsNullabilitySufficient(from: KSType?, to: KSType?): Boolean {
    if (from == null || to == null) throw IllegalStateException("Types can not be null")
    val fromArgument = from.arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${from.declaration.simpleName.asString()}")
    val toArgument = to.arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${to.declaration.simpleName.asString()}")
    return checkDifferentTypesNullabilitySufficient(fromArgument, toArgument)
}

internal fun checkMapCollectionTypeArgumentsNullabilitySufficient(from: KSType?, to: KSType?): Boolean {
    if (from == null || to == null) throw IllegalStateException("Types can not be null")
    val fromArgument = from.arguments.getOrNull(1)?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${from.declaration.simpleName.asString()}")
    val toArgument = to.arguments.getOrNull(1)?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${to.declaration.simpleName.asString()}")
    return checkDifferentTypesNullabilitySufficient(fromArgument, toArgument)
}

internal fun checkMapCollectionTypeKeyArgumentsNullabilitySufficient(from: KSType?, to: KSType?): Boolean {
    if (from == null || to == null) throw IllegalStateException("Types can not be null")
    val fromKeyArgument = from.arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${from.declaration.simpleName.asString()}")
    val toKeyArgument = to.arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${to.declaration.simpleName.asString()}")
    return checkDifferentTypesNullabilitySufficient(fromKeyArgument, toKeyArgument)
}

internal fun checkDifferentTypesNullabilitySufficient(from: KSType?, to: KSType?): Boolean {
    if (from == null || to == null) throw IllegalStateException("Types can not be null")
    return from.nullability == Nullability.NOT_NULL || (from.nullability == Nullability.NULLABLE && to.nullability == Nullability.NULLABLE)
}

internal fun checkSameTypeWithNullabilitySufficient(from: KSType?, to: KSType?): Boolean {
    if (from == null || to == null) throw IllegalStateException("Types can not be null")
    return from == to || from == to.makeNotNullable()
}

internal fun KSType.isCollectionTypeArgumentDataClass(): Boolean =
    this.isSupportedCollectionType() &&
            this.arguments.firstOrNull()?.type?.resolve()?.isDataClass() ?: false

internal fun KSType.extractSupportedCollectionTypeArgumentType(): KSType =
   arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Can't find collection type argument for ${this.toClassName().simpleName}")

internal fun KSType.extractSupportedMapCollectionTypeArgument(): KSType =
    arguments.getOrNull(1)?.type?.resolve() ?: throw IllegalStateException("Can't find map collection type argument for ${this.toClassName().simpleName}")

internal fun KSType.extractSupportedMapCollectionKeyTypeArgument(): KSType =
    arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Can't find map collection type argument for ${this.toClassName().simpleName}")

internal fun KSType.findOptInAnnotations(): List<KSAnnotation> {
    val declaration = this.declaration as? KSClassDeclaration ?: return emptyList()
    return declaration.annotations.filter {
        it.annotationType.resolve()
            .declaration.annotations.any { meta ->
                meta.annotationType.resolve().declaration.qualifiedName?.asString() == "kotlin.RequiresOptIn"
            }
    }.toList()
}

internal fun KSType.collectRequiredOptIns(): List<ClassName> {
    return findOptInAnnotations().mapNotNull {
        val annotationType = it.annotationType.resolve()
        annotationType.declaration.qualifiedName?.asString()?.let { fqName ->
            ClassName.bestGuess(fqName)
        }
    }
}

internal fun KSType.collectRequiredOptInsDeep(): Set<ClassName> {
    val visited = mutableSetOf<KSType>()
    val result = mutableSetOf<ClassName>()

    fun recurse(type: KSType) {
        if (!visited.add(type)) return

        // Сначала аннотации самого типа
        result += type.collectRequiredOptIns()

        val decl = type.declaration as? KSClassDeclaration ?: return

        // Рекурсивно пройтись по свойствам
        decl.getAllProperties().forEach { property ->
            val propType = property.type.resolve()
            recurse(propType)
        }
    }

    recurse(this)
    return result
}
