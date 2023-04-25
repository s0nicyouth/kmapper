package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.syouth.kmapper.processor.base.*
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.base.checkSameTypeWithNullabilitySufficient
import com.syouth.kmapper.processor.base.isDataClass
import com.syouth.kmapper.processor.base.isSupportedCollectionType
import com.syouth.kmapper.processor.convertors.models.AssignableStatement

internal class NonCollectionTypeConverter : TypeConvertor {

    override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean {
        if (from == null) return false
        if (from.isSupportedCollectionType() || to.isSupportedCollectionType()) return false
        if (from.isSupportedMapCollectionType() || to.isSupportedMapCollectionType()) return false
        if (from.isDataClass() || to.isDataClass()) return false
        return checkSameTypeWithNullabilitySufficient(from, to)
    }

    override fun buildConversionStatement(
        fromParameterSpec: ParameterSpec?,
        from: KSType?,
        to: KSType,
        targetPath: PathHolder?,
        bundle: Bundle
    ): AssignableStatement = AssignableStatement(
            code = buildCodeBlock {
                check(fromParameterSpec != null) { "from object name can't be null here" }
                add("%N", fromParameterSpec)
            },
        requiresObjectToConvertFrom = true
    )
}