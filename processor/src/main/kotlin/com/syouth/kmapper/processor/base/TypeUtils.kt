package com.syouth.kmapper.processor.base

import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.data.SUPPORTED_CONVERSION_INTERFACES
import com.syouth.kmapper.processor.base.data.SUPPORTED_CONVERSION_INTERFACES_CORRESPONDING_CONCRETE_TYPES

internal fun KSType.isDataClass() =
    declaration.modifiers.contains(Modifier.DATA)

internal fun KSType.isSupportedCollectionType() =
    declaration.qualifiedName?.asString() in SUPPORTED_CONVERSION_INTERFACES

internal fun areSameSupportedCollectionTypes(first: KSType, second: KSType): Boolean {
    if (first.declaration.qualifiedName?.asString() != second.declaration.qualifiedName?.asString()) return false
    if (first.declaration.qualifiedName?.asString() !in SUPPORTED_CONVERSION_INTERFACES) return false
    if (first.arguments.size != second.arguments.size) return false
    if (!first.arguments.zip(second.arguments).all { (f, s) -> checkSameTypeWithNullabilitySufficient(f.type?.resolve(), s.type?.resolve()) }) return false
    return true
}

internal fun KSType.getCorrespondingConcreteTypeForSupportedCollectionType(): TypeName {
    val qualifiedName = declaration.qualifiedName?.asString() ?: throw IllegalStateException("Can't resolve qualified name")
    if (qualifiedName in SUPPORTED_CONVERSION_INTERFACES) {
        val concreteType = SUPPORTED_CONVERSION_INTERFACES_CORRESPONDING_CONCRETE_TYPES[qualifiedName] ?: throw IllegalStateException("Can't find concrete type for collection type")
        val indexOfLastDot = concreteType.lastIndexOf(".")
        if (indexOfLastDot == -1) throw IllegalStateException("Wrong type qualified name")
        val packageName = concreteType.substring(0, indexOfLastDot)
        val typeName = concreteType.substring(indexOfLastDot + 1)
        return ClassName(packageName, typeName).parameterizedBy(arguments[0].toTypeName())
    } else {
        TODO()
    }
}

internal fun checkCollectionArgumentsNullabilitySufficient(from: KSType?, to: KSType?): Boolean {
    if (from == null || to == null) throw IllegalStateException("Types can not be null")
    val fromArgument = from.arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${from.declaration.simpleName.asString()}")
    val toArgument = to.arguments.firstOrNull()?.type?.resolve() ?: throw IllegalStateException("Collection should have arguments ${to.declaration.simpleName.asString()}")
    return checkDifferentTypesNullabilitySufficient(fromArgument, toArgument)
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