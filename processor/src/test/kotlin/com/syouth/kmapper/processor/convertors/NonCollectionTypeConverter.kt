package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ParameterSpec
import com.syouth.kmapper.processor.base.Bundle
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.base.data.SUPPORTED_CONVERSION_INTERFACES
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class NonCollectionTypeConverterTest {

    private val nameCollectionType: KSName = mock {
        on { asString() } doReturn SUPPORTED_CONVERSION_INTERFACES.first()
    }
    private val declarationCollectionType: KSClassDeclaration = mock {
        on { simpleName } doReturn nameCollectionType
    }
    private val collectionType: KSType = mock {
        on { declaration } doReturn declarationCollectionType
    }
    private val nameNonCollectionType: KSName = mock {
        on { asString() } doReturn "test.com.class"
    }
    private val declarationNonCollectionType: KSClassDeclaration = mock {
        on { simpleName } doReturn nameNonCollectionType
    }
    private val sufficientType: KSType = mock {
        on { declaration } doReturn declarationNonCollectionType
        on { makeNotNullable() } doReturn it
    }
    private val dataClassDeclaration: KSDeclaration = mock { on { modifiers } doReturn setOf(Modifier.DATA) }
    private val dataClassType: KSType = mock {
        on { declaration } doReturn dataClassDeclaration
    }

    private val converter = NonCollectionTypeConverter()

    @Test
    fun `GIVEN from type is null then conversion not supported`() {
        Assertions.assertFalse(converter.isSupported(null, mock(), PathHolder()))
    }

    @Test
    fun `GIVEN from is collection to is not THEN false returned`() {
        Assertions.assertFalse(converter.isSupported(collectionType, sufficientType, PathHolder()))
    }

    @Test
    fun `GIVEN from is not collection to is THEN false returned`() {
        Assertions.assertFalse(converter.isSupported(sufficientType, collectionType, PathHolder()))
    }

    @Test
    fun `GIVEN from is data class THEN false returned`() {
        Assertions.assertFalse(converter.isSupported(dataClassType, sufficientType, PathHolder()))
    }

    @Test
    fun `GIVEN to is data class THEN false returned`() {
        Assertions.assertFalse(converter.isSupported(sufficientType, dataClassType, PathHolder()))
    }

    @Test
    fun `GIVEN both types fit THEN true returned`() {
        Assertions.assertTrue(converter.isSupported(sufficientType, sufficientType, PathHolder()))
    }

    @Test
    fun `GIVEN fromParameterSpec is null THEN exception is thrown`() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            converter.buildConversionStatement(
                null,
                mock(),
                mock(),
                PathHolder(),
                Bundle()
            )
        }
    }

    @Test
    fun `GIVEN parameters are correct THEN generated code is correct`() {
        val parameterSpec = ParameterSpec.builder("it", Int::class).build()
        val assignableStatement = converter.buildConversionStatement(parameterSpec, mock(), mock(), PathHolder(), Bundle())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("it", assignableStatement.code.toString())
    }
}