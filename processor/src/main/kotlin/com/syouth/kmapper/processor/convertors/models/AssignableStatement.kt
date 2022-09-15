package com.syouth.kmapper.processor.convertors.models

import com.squareup.kotlinpoet.CodeBlock

internal data class AssignableStatement(
    val code: CodeBlock,
    val requiresObjectToConvertFrom: Boolean
)