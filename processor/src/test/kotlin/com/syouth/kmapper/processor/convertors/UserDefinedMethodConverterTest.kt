package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ParameterSpec
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.testutils.mockKSFunctionDeclaration
import com.syouth.kmapper.processor.testutils.mockKSType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class UserDefinedMethodConverterTest {

    private val from: KSType = mockKSType(qualifiedName = "com.syouth.test.type1")
    private val to: KSType = mockKSType(qualifiedName = "com.syouth.test.type2")
    private val function: KSFunctionDeclaration = mockKSFunctionDeclaration()
    private val converter = UserDefinedMethodConverter(from, to, function)

    @Test
    fun `GIVEN from different from expected type THEN false returned`() {
        Assertions.assertFalse(converter.isSupported(mock(), to, PathHolder()))
    }

    @Test
    fun `GIVEN to different from expected type THEN false returned`() {
        Assertions.assertFalse(converter.isSupported(from, mock(), PathHolder()))
    }

    @Test
    fun `GIVEN to and from are correct THEN true returned`() {
        Assertions.assertTrue(converter.isSupported(from, to, PathHolder()))
    }

    @Test
    fun `GIVEN fromParameterSpec is null THEN exception is thrown`() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            converter.buildConversionStatement(null, from, to, PathHolder())
        }
    }

    @Test
    fun `GIVEN all parameters are correct then generated code is correct`() {
        val parameterSpec = ParameterSpec.builder("it", Int::class.java).build()
        val assignableStatement = converter.buildConversionStatement(parameterSpec, from, to, PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("map(it)", assignableStatement.code.toString())
    }

    @Test
    fun `GIVEN from is nullable and function's from is not THEN false is returned`() {
        val from: KSType = mockKSType(qualifiedName = "com.syouth.test.type1")
        val to: KSType = mockKSType(qualifiedName = "com.syouth.test.type2")
        val function: KSFunctionDeclaration = mockKSFunctionDeclaration()
        val converter = UserDefinedMethodConverter(from, to, function)
        Assertions.assertFalse(converter.isSupported(from.makeNullable(), to, PathHolder()))
    }

    @Test
    fun `GIVEN from is nullable and function's from is nullable THEN true is returned`() {
        val from: KSType = mockKSType(qualifiedName = "com.syouth.test.type1").makeNullable()
        val to: KSType = mockKSType(qualifiedName = "com.syouth.test.type2")
        val function: KSFunctionDeclaration = mockKSFunctionDeclaration()
        val converter = UserDefinedMethodConverter(from, to, function)
        Assertions.assertTrue(converter.isSupported(from.makeNullable(), to, PathHolder()))
    }

    @Test
    fun `GIVEN from is not nullable and function's from is not nullable THEN true is returned`() {
        val from: KSType = mockKSType(qualifiedName = "com.syouth.test.type1")
        val to: KSType = mockKSType(qualifiedName = "com.syouth.test.type2")
        val function: KSFunctionDeclaration = mockKSFunctionDeclaration()
        val converter = UserDefinedMethodConverter(from, to, function)
        Assertions.assertTrue(converter.isSupported(from, to, PathHolder()))
    }

    @Test
    fun `GIVEN from is not nullable and function's from is nullable THEN true is returned`() {
        val from: KSType = mockKSType(qualifiedName = "com.syouth.test.type1")
        val to: KSType = mockKSType(qualifiedName = "com.syouth.test.type2")
        val function: KSFunctionDeclaration = mockKSFunctionDeclaration()
        val converter = UserDefinedMethodConverter(from.makeNullable(), to, function)
        Assertions.assertTrue(converter.isSupported(from, to, PathHolder()))
    }

    @Test
    fun `GIVEN function's to is nullable and to is not THEN code generation is correct`() {
        val from: KSType = mockKSType(qualifiedName = "com.syouth.test.type1")
        val to: KSType = mockKSType(qualifiedName = "com.syouth.test.type2", nullability = Nullability.NULLABLE)
        val function: KSFunctionDeclaration = mockKSFunctionDeclaration()
        val converter = UserDefinedMethodConverter(from, to, function)
        val parameterSpec = ParameterSpec.builder("it", Int::class.java).build()
        val assignableStatement = converter.buildConversionStatement(parameterSpec, from, to.makeNotNullable(), PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                map(it) ?: throw kotlin.IllegalStateException("Can not assign null to non null value")
            """.trimIndent(),
            assignableStatement.code.toString()
        )
    }
}