package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement
import com.syouth.kmapper.processor.testutils.mockKSType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class UserDefinedPropertyConverterTest {

    private val converterManager: ConvertersManager = mock {
    }
    private val targetPath = PathHolder().apply {
        appendPathElement(PathHolder.PathElement("test", mockKSType(qualifiedName = "com.syouth.test.type1")))
        appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
    }
    private val sourcePath = PathHolder().apply {
        appendPathElement(PathHolder.PathElement("source", mockKSType(qualifiedName = "com.syouth.test.type3")))
        appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type4")))
    }
    private val converter = UserDefinedPropertyConverter(
        convertersManager = converterManager,
        targetPath = targetPath,
        sourcePath = sourcePath
    )

    @Test
    fun `GIVEN targetPath is null THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(null, mock(), null))
    }

    @Test
    fun `GIVEN targetPath is is not null WHEN targetPath is not equal to expected THEN false is returned`() {
        val targetPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("some", mockKSType(qualifiedName = "com.syouth.test.type1")))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
        }
        Assertions.assertFalse(converter.isSupported(null, mock(), targetPath))
    }

    @Test
    fun `GIVEN targetPath is is not null WHEN targetPath is equal to expected but from is nullable and to is not THEN false is returned`() {
        val targetPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("test", mockKSType(qualifiedName = "com.syouth.test.type1")))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
        }
        sourcePath.removeLastPathElement()
        sourcePath.removeLastPathElement()
        sourcePath.apply {
            appendPathElement(PathHolder.PathElement("source", mockKSType(qualifiedName = "com.syouth.test.type3", nullability = Nullability.NULLABLE)))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type4")))
        }
        Assertions.assertFalse(converter.isSupported(null, mockKSType(), targetPath))
    }

    @Test
    fun `GIVEN all parameters are correct WHEN from and to are not nullable THEN true is returned`() {
        val targetPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("test", mockKSType(qualifiedName = "com.syouth.test.type1")))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
        }
        Assertions.assertTrue(converter.isSupported(null, mockKSType(qualifiedName = "com.syouth.test.type2"), targetPath))
    }

    @Test
    fun `GIVEN all parameters are correct WHEN from is not nullable and to is nullable THEN true is returned`() {
        val targetPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("test", mockKSType(qualifiedName = "com.syouth.test.type1")))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
        }
        Assertions.assertTrue(converter.isSupported(null, mockKSType(qualifiedName = "com.syouth.test.type2", nullability = Nullability.NULLABLE), targetPath))
    }

    @Test
    fun `GIVEN all parameters are correct WHEN from and to are nullable THEN true is returned`() {
        val targetPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("test", mockKSType(qualifiedName = "com.syouth.test.type1")))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
        }
        sourcePath.removeLastPathElement()
        sourcePath.removeLastPathElement()
        sourcePath.apply {
            appendPathElement(PathHolder.PathElement("source", mockKSType(qualifiedName = "com.syouth.test.type3", nullability = Nullability.NULLABLE)))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type4")))
        }
        Assertions.assertTrue(converter.isSupported(null, mockKSType(qualifiedName = "com.syouth.test.type2", nullability = Nullability.NULLABLE), targetPath))
    }

    @Test
    fun `GIVEN all parameters are correct WHEN from is nullable and to is not nullable THEN false is returned`() {
        val targetPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("test", mockKSType(qualifiedName = "com.syouth.test.type1")))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type2")))
        }
        sourcePath.removeLastPathElement()
        sourcePath.removeLastPathElement()
        sourcePath.apply {
            appendPathElement(PathHolder.PathElement("source", mockKSType(qualifiedName = "com.syouth.test.type3", nullability = Nullability.NULLABLE)))
            appendPathElement(PathHolder.PathElement("path", mockKSType(qualifiedName = "com.syouth.test.type4")))
        }
        Assertions.assertFalse(converter.isSupported(null, mockKSType(qualifiedName = "com.syouth.test.type2"), targetPath))
    }

    @Test
    fun `GIVEN all parameters are correct and internal converter doesn't require external parameter THEN generated code is correct`() {
        val internalConverter = object : TypeConvertor {
            override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean = true

            override fun buildConversionStatement(
                fromParameterSpec: ParameterSpec?,
                from: KSType?,
                to: KSType,
                targetPath: PathHolder?
            ): AssignableStatement = AssignableStatement(
                code = buildCodeBlock {
                    add("10")
                },
                requiresObjectToConvertFrom = false
            )
        }
        whenever(converterManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val assignableStatement = converter.buildConversionStatement(null, null, mockKSType(qualifiedName = "com.syouth.test.type2"), targetPath)
        Assertions.assertFalse(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("10", assignableStatement.code.toString())
    }

    @Test
    fun `GIVEN all parameters are correct and internal converter requires external parameter THEN generated code is correct`() {
        val internalConverter = object : TypeConvertor {
            override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean = true

            override fun buildConversionStatement(
                fromParameterSpec: ParameterSpec?,
                from: KSType?,
                to: KSType,
                targetPath: PathHolder?
            ): AssignableStatement = AssignableStatement(
                code = buildCodeBlock {
                    add("it")
                },
                requiresObjectToConvertFrom = true
            )
        }
        whenever(converterManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val assignableStatement = converter.buildConversionStatement(null, null, mockKSType(qualifiedName = "com.syouth.test.type2"), targetPath)
        Assertions.assertFalse(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("source.path.let {\n  it\n}\n", assignableStatement.code.toString())
    }

    @Test
    fun `GIVEN all parameters are correct and internal converter requires external parameter WHEN source is nullable THEN generated code is correct`() {

        val internalConverter = object : TypeConvertor {
            override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean = true

            override fun buildConversionStatement(
                fromParameterSpec: ParameterSpec?,
                from: KSType?,
                to: KSType,
                targetPath: PathHolder?
            ): AssignableStatement = AssignableStatement(
                code = buildCodeBlock {
                    add("it")
                },
                requiresObjectToConvertFrom = true
            )
        }
        whenever(converterManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val assignableStatement = converter.buildConversionStatement(null, null, mockKSType(qualifiedName = "com.syouth.test.type2"), targetPath)
        Assertions.assertFalse(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("source.path.let {\n  it\n}\n", assignableStatement.code.toString())
    }
}