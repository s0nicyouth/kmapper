package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement
import com.syouth.kmapper.processor.testutils.mockKSProperty
import com.syouth.kmapper.processor.testutils.mockKSType
import com.syouth.kmapper.processor.testutils.mockKValueParameter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.IllegalStateException

internal class DataClassTypeConverterTest {

    private val floatType = mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float")
    private val floatValueParam = mockKValueParameter("fType", floatType)
    private val floatProperTy = mockKSProperty("fType", floatType)
    private val fromDataType = mockKSType(modifiers = setOf(Modifier.DATA), declarationProperties = listOf(floatProperTy))
    private val toDataType = mockKSType(packageName = "fake", qualifiedName = "cls", modifiers = setOf(Modifier.DATA), constructorParams = listOf(floatValueParam))
    private val convertersManager: ConvertersManager = mock {

    }
    private val converter = DataClassTypeConverter(convertersManager)

    @Test
    fun `GIVEN from type is null THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(null, toDataType, PathHolder()))
    }

    @Test
    fun `GIVEN all data is correct WHEN from type is nullable and to is not THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(fromDataType.makeNullable(), toDataType, PathHolder()))
    }

    @Test
    fun `GIVEN all data correct WHEN from and to is not nullable THEN true is returned`() {
        Assertions.assertTrue(converter.isSupported(fromDataType, toDataType, PathHolder()))
    }

    @Test
    fun `GIVEN all data correct WHEN from and to is nullable THEN true is returned`() {
        Assertions.assertTrue(converter.isSupported(fromDataType.makeNullable(), toDataType.makeNullable(), PathHolder()))
    }

    @Test
    fun `GIVEN all data correct WHEN from is not nullable and to is nullable THEN true is returned`() {
        Assertions.assertTrue(converter.isSupported(fromDataType, toDataType.makeNullable(), PathHolder()))
    }

    @Test
    fun `GIVEN from type equals to to type THEN code generation is correct`() {
        val paramSpec = ParameterSpec.builder("it", mockKSType().toTypeName()).build()
        val assignableStatement = converter.buildConversionStatement(paramSpec, fromDataType, fromDataType, PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("it", assignableStatement.code.toString())
    }

    @Test
    fun `GIVEN from type equals to to nullable type THEN code generation is correct`() {
        val paramSpec = ParameterSpec.builder("it", mockKSType().toTypeName()).build()
        val assignableStatement = converter.buildConversionStatement(paramSpec, fromDataType.makeNotNullable(), fromDataType.makeNullable(), PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals("it", assignableStatement.code.toString())
    }

    @Test
    fun `GIVEN both from and to are not nullable THEN code generation is correct`() {
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
        whenever(convertersManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val paramSpec = ParameterSpec.builder("it", mockKSType(modifiers = setOf(Modifier.DATA)).toTypeName()).build()
        val assignableStatement = converter.buildConversionStatement(paramSpec, fromDataType, toDataType, PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                fake.cls(
                  fType = it.fType.let {
                    it
                  }
                )
                
            """.trimIndent(),
            assignableStatement.code.toString()
        )
    }

    @Test
    fun `GIVEN from is nullable THEN code generation is correct`() {
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
        whenever(convertersManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val paramSpec = ParameterSpec.builder("it", mockKSType(modifiers = setOf(Modifier.DATA)).toTypeName()).build()
        val assignableStatement = converter.buildConversionStatement(paramSpec, fromDataType.makeNullable(), toDataType.makeNullable(), PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                if (it == null) {
                  null
                } else {
                  fake.cls(
                    fType = it.fType.let {
                      it
                    }
                  )
                }
                
            """.trimIndent(),
            assignableStatement.code.toString()
        )
    }

    @Test
    fun `GIVEN from is nullable an to is not THEN exception is thrown`() {
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
        whenever(convertersManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val paramSpec = ParameterSpec.builder("it", mockKSType(modifiers = setOf(Modifier.DATA)).toTypeName()).build()
        Assertions.assertThrows(IllegalStateException::class.java) {
            converter.buildConversionStatement(paramSpec, fromDataType.makeNullable(), toDataType, PathHolder())
        }
    }

    @Test
    fun `GIVEN from is nullable WHEN more then two params in class THEN code generation is correct`() {
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
        whenever((fromDataType.declaration as  KSClassDeclaration).getAllProperties()).thenReturn(listOf(floatProperTy, floatProperTy).asSequence())
        whenever((toDataType.declaration as KSClassDeclaration).primaryConstructor!!.parameters).thenReturn(listOf(floatValueParam, floatValueParam))
        whenever(convertersManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val paramSpec = ParameterSpec.builder("it", mockKSType(modifiers = setOf(Modifier.DATA)).toTypeName()).build()
        val assignableStatement = converter.buildConversionStatement(paramSpec, fromDataType.makeNullable(), toDataType.makeNullable(), PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                if (it == null) {
                  null
                } else {
                  fake.cls(
                    fType = it.fType.let {
                      it
                    }
                    ,
                    fType = it.fType.let {
                      it
                    }
                  )
                }
                
            """.trimIndent(),
            assignableStatement.code.toString()
        )
    }

    @Test
    fun `GIVEN from is not nullable WHEN input param is not needed THEN code generation is correct`() {
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
                requiresObjectToConvertFrom = false
            )
        }
        whenever(convertersManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val paramSpec = ParameterSpec.builder("it", mockKSType(modifiers = setOf(Modifier.DATA)).toTypeName()).build()
        val assignableStatement = converter.buildConversionStatement(paramSpec, fromDataType, toDataType, PathHolder())
        Assertions.assertTrue(assignableStatement.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                fake.cls(
                  fType = it
                )
                
            """.trimIndent(),
            assignableStatement.code.toString()
        )
    }
}
