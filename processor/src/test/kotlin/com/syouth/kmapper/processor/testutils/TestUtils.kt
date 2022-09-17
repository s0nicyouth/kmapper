package com.syouth.kmapper.processor.testutils

import com.google.devtools.ksp.symbol.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal fun mockKSType(
    packageName: String = "com.syouth.test",
    qualifiedName: String = "com.syouth.test.type",
    nullability: Nullability = Nullability.NOT_NULL
): KSType {
    val packageName: KSName = mock {
        on { asString() } doReturn packageName
    }
    val qualifiedName: KSName = mock {
        on { asString() } doReturn qualifiedName
    }
    val declaration: KSClassDeclaration = mock() {
        on { this.packageName } doReturn packageName
        on { this.qualifiedName } doReturn qualifiedName
    }

    val nullable: KSType = mock() {
        on { isError } doReturn false
        on { this.declaration } doReturn declaration
        on { arguments } doReturn emptyList()
        on { this.nullability } doReturn Nullability.NULLABLE
    }

    val nonNullable: KSType = mock() {
        on { isError } doReturn false
        on { this.declaration } doReturn declaration
        on { arguments } doReturn emptyList()
        on { this.nullability } doReturn Nullability.NOT_NULL
    }

    return mock() {
        on { isError } doReturn false
        on { this.declaration } doReturn declaration
        on { arguments } doReturn emptyList()
        on { this.nullability } doReturn nullability
        on { makeNullable() } doReturn nullable
        on { makeNotNullable() } doReturn nonNullable
    }
}

internal fun mockKSFunctionDeclaration(name: String = "map"): KSFunctionDeclaration {
    val name: KSName = mock {
        on { asString() } doReturn name
    }
    return mock {
        on { simpleName } doReturn name
    }
}