package com.syouth.kmapper.processor.testutils

import com.google.devtools.ksp.symbol.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal fun mockKSType(
    packageName: String = "com.syouth.test",
    qualifiedName: String = "com.syouth.test.type",
    nullability: Nullability = Nullability.NOT_NULL,
    arguments: List<KSTypeArgument> = emptyList(),
    modifiers: Set<Modifier> = emptySet(),
    declarationProperties: List<KSPropertyDeclaration> = emptyList(),
    constructorParams: List<KSValueParameter> = emptyList()
): KSType {
    val packageName: KSName = mock {
        on { asString() } doReturn packageName
    }
    val qualifiedName: KSName = mock {
        on { asString() } doReturn qualifiedName
    }
    val primaryConstructor = mockKSFunctionDeclaration(valueParameters = constructorParams)
    val declaration: KSClassDeclaration = mock() {
        on { this.packageName } doReturn packageName
        on { this.qualifiedName } doReturn qualifiedName
        on { this.modifiers } doReturn modifiers
        on { this.getAllProperties() } doReturn declarationProperties.asSequence()
        on { this.primaryConstructor } doReturn primaryConstructor
    }

    val nullable: KSType = mock() {
        on { isError } doReturn false
        on { this.declaration } doReturn declaration
        on { this.arguments } doReturn arguments
        on { this.nullability } doReturn Nullability.NULLABLE
    }

    val nonNullable: KSType = mock() {
        on { isError } doReturn false
        on { this.declaration } doReturn declaration
        on { this.arguments } doReturn arguments
        on { this.nullability } doReturn Nullability.NOT_NULL
    }

    whenever(nullable.makeNotNullable()).thenReturn(nonNullable)
    whenever(nullable.makeNullable()).thenReturn(nullable)
    whenever(nonNullable.makeNullable()).thenReturn(nullable)
    whenever(nonNullable.makeNotNullable()).thenReturn(nonNullable)

    return if (nullability == Nullability.NULLABLE) nullable else nonNullable
}

internal fun mockKSFunctionDeclaration(
    name: String = "map",
    valueParameters: List<KSValueParameter> = emptyList()
): KSFunctionDeclaration {
    val name: KSName = mock {
        on { asString() } doReturn name
    }
    return mock {
        on { simpleName } doReturn name
        on { this.parameters } doReturn valueParameters
    }
}

internal fun mockKSTypeArgument(argType: KSType): KSTypeArgument {
    val typeReference: KSTypeReference = mock {
        on { resolve() } doReturn argType
    }
    return mock {
        on { type } doReturn typeReference
        on { variance } doReturn Variance.COVARIANT
    }
}

internal fun mockKValueParameter(nameString: String, type: KSType): KSValueParameter {
    val name: KSName = mock {
        on { asString() } doReturn nameString
    }
    val typeReference: KSTypeReference = mock {
        on { resolve() } doReturn type
    }
    return mock {
        on { this.name } doReturn name
        on { this.type } doReturn typeReference
    }
}

internal fun mockKSProperty(nameString: String, type: KSType): KSPropertyDeclaration {
    val name: KSName = mock {
        on { asString() } doReturn nameString
    }
    val typeReference: KSTypeReference = mock {
        on { resolve() } doReturn type
    }
    return mock {
        on { simpleName } doReturn name
        on { this.type } doReturn typeReference
    }
}