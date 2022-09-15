package com.syouth.kmapper.processor.convertors.manager

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.convertors.TypeConvertor

internal interface ConvertersManager {
    fun findConverterForTypes(from: KSType?, to: KSType, targetPath: PathHolder?): TypeConvertor?
    fun initializeForMapperClass(mapper: KSClassDeclaration)
    fun initializeForMapperFunction(func: KSFunctionDeclaration)
}