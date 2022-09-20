package com.syouth.kmapper.processor.convertors

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.convertors.models.AssignableStatement
import com.syouth.kmapper.processor.testutils.mockKSType
import com.syouth.kmapper.processor.testutils.mockKSTypeArgument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class CollectionTypeConverterTest {

    private val firstCollectionTypeFloatNullable: KSType = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.MutableList",
        arguments = listOf(mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int", nullability = Nullability.NULLABLE)))
    )
    private val secondCollectionTypeIntNullable: KSType = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.List",
        arguments = listOf(mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int", nullability = Nullability.NULLABLE)))
    )
    private val firstCollectionTypeFloat: KSType = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.MutableList",
        arguments = listOf(mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int")))
    )
    private val secondCollectionTypeInt: KSType = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.List",
        arguments = listOf(mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int")))
    )
    private val unsupportedType: KSType = mockKSType()

    private val convertersManager: ConvertersManager = mock {
        Int
    }
    private val convertor = CollectionTypeConverter(convertersManager)

    @Test
    fun `GIVEN from is null THEN false is returned`() {
        Assertions.assertFalse(convertor.isSupported(null, firstCollectionTypeFloat, PathHolder()))
    }

    @Test
    fun `GIVEN from is not supported collection THEN false is returned`() {
        Assertions.assertFalse(convertor.isSupported(unsupportedType, firstCollectionTypeFloat, PathHolder()))
    }

    @Test
    fun `GIVEN to is not supported collection THEN false is returned`() {
        Assertions.assertFalse(convertor.isSupported(firstCollectionTypeFloat, unsupportedType, PathHolder()))
    }

    @Test
    fun `GIVEN collection type arguments nullability is wrong THEN false is returned`() {
        Assertions.assertFalse(convertor.isSupported(firstCollectionTypeFloatNullable, secondCollectionTypeInt, PathHolder()))
    }

    @Test
    fun `GIVEN all arguments are correct WHEN type argument from is not null and to is not null THEN true is returned`() {
        Assertions.assertTrue(convertor.isSupported(firstCollectionTypeFloat, secondCollectionTypeInt, PathHolder()))
    }

    @Test
    fun `GIVEN all arguments are correct WHEN type argument from is null and to is null THEN true is returned`() {
        Assertions.assertTrue(convertor.isSupported(firstCollectionTypeFloatNullable, secondCollectionTypeIntNullable, PathHolder()))
    }

    @Test
    fun `GIVEN all arguments are correct WHEN type argument from is not null and to is null THEN true is returned`() {
        Assertions.assertTrue(convertor.isSupported(firstCollectionTypeFloat, secondCollectionTypeIntNullable, PathHolder()))
    }

    @Test
    fun `GIVEN from is nullable and to is not THEN false is returned`() {
        Assertions.assertFalse(convertor.isSupported(firstCollectionTypeFloat.makeNullable(), secondCollectionTypeInt, PathHolder()))
    }

    @Test
    fun `GIVEN from is nullable and to is THEN true is returned`() {
        Assertions.assertTrue(convertor.isSupported(firstCollectionTypeFloat.makeNullable(), secondCollectionTypeInt.makeNullable(), PathHolder()))
    }

    @Test
    fun `GIVEN from is not nullable and to is THEN true is returned`() {
        Assertions.assertTrue(convertor.isSupported(firstCollectionTypeFloat, secondCollectionTypeInt.makeNullable(), PathHolder()))
    }

    @Test
    fun `GIVEN from is not nullable and to is not THEN true is returned`() {
        Assertions.assertTrue(convertor.isSupported(firstCollectionTypeFloat, secondCollectionTypeInt, PathHolder()))
    }

    @Test
    fun `GIVEN same argument types THEN generated code is correct`() {
        val parameterSpec = ParameterSpec.builder("it", firstCollectionTypeFloat.toTypeName()).build()
        val result = convertor.buildConversionStatement(parameterSpec, firstCollectionTypeFloat, firstCollectionTypeFloat, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals("it", result.code.toString())
    }

    @Test
    fun `GIVEN different argument types WHEN from is nullable THEN generated code is correct`() {
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
        val parameterSpec = ParameterSpec.builder("it", firstCollectionTypeFloat.toTypeName()).build()
        val result = convertor.buildConversionStatement(parameterSpec, firstCollectionTypeFloat.makeNullable(), secondCollectionTypeInt.makeNullable(), PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  if (it == null) {
                    return@run null
                  }
                  val result = kotlin.collections.ArrayList<out kotlin.Int>()
                  for (obj in it) {
                    val converted = obj.let {
                      it
                    }
                    result += converted
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
    }

    @Test
    fun `GIVEN different argument types WHEN type argument of from is nullable THEN generated code is correct`() {
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
        val parameterSpec = ParameterSpec.builder("it", firstCollectionTypeFloat.toTypeName()).build()
        val result = convertor.buildConversionStatement(parameterSpec, firstCollectionTypeFloatNullable, secondCollectionTypeIntNullable, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  val result = kotlin.collections.ArrayList<out kotlin.Int>()
                  for (obj in it) {
                    if (obj == null) {
                      result += null
                      continue
                    }
                    val converted = obj.let {
                      it
                    }
                    result += converted
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
    }

    @Test
    fun `GIVEN different argument types WHEN external argument for type argument is not needed THEN generated code is correct`() {
        val internalConverter = object : TypeConvertor {
            override fun isSupported(from: KSType?, to: KSType, targetPath: PathHolder?): Boolean = true

            override fun buildConversionStatement(
                fromParameterSpec: ParameterSpec?,
                from: KSType?,
                to: KSType,
                targetPath: PathHolder?
            ): AssignableStatement = AssignableStatement(
                code = buildCodeBlock {
                    add("someObject")
                },
                requiresObjectToConvertFrom = false
            )
        }
        whenever(convertersManager.findConverterForTypes(anyOrNull(), any(), anyOrNull())).thenReturn(internalConverter)
        val parameterSpec = ParameterSpec.builder("it", firstCollectionTypeFloat.toTypeName()).build()
        val result = convertor.buildConversionStatement(parameterSpec, firstCollectionTypeFloat, secondCollectionTypeInt, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  val result = kotlin.collections.ArrayList<out kotlin.Int>()
                  for (obj in it) {
                    val converted = someObject
                    result += converted
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
    }
}