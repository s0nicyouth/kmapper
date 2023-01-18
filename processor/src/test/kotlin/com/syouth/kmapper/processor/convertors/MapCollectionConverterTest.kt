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

internal class MapCollectionConverterTest {

    private val mapCollectionTypeFloatInt = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.Map",
        arguments = listOf(
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float", nullability = Nullability.NOT_NULL)),
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int", nullability = Nullability.NOT_NULL))
        )
    )
    private val mapCollectionTypeFloatChar = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.Map",
        arguments = listOf(
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float", nullability = Nullability.NOT_NULL)),
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Char", nullability = Nullability.NOT_NULL))
        )
    )
    private val mapCollectionTypeFloatIntNullableValueArgument = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.Map",
        arguments = listOf(
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float", nullability = Nullability.NOT_NULL)),
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int", nullability = Nullability.NULLABLE))
        )
    )
    private val mapCollectionTypeFloatCharNullableValueArgument = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.Map",
        arguments = listOf(
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float", nullability = Nullability.NOT_NULL)),
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Char", nullability = Nullability.NULLABLE))
        )
    )
    private val mapCollectionTypeFloatIntNullable = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.Map",
        arguments = listOf(
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float", nullability = Nullability.NOT_NULL)),
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Int", nullability = Nullability.NOT_NULL))
        ),
        nullability = Nullability.NULLABLE
    )
    private val mapCollectionTypeFloatCharNullable = mockKSType(
        packageName = "kotlin.collections",
        qualifiedName = "kotlin.collections.Map",
        arguments = listOf(
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Float", nullability = Nullability.NOT_NULL)),
            mockKSTypeArgument(mockKSType(packageName = "kotlin", qualifiedName = "kotlin.Char", nullability = Nullability.NOT_NULL))
        ),
        nullability = Nullability.NULLABLE
    )
    private val unsupportedType: KSType = mockKSType()
    private val convertersManager: ConvertersManager = mock()
    private val converter = MapCollectionConverter(
        convertersManager = convertersManager
    )

    @Test
    fun `GIVEN from is null THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(null, mapCollectionTypeFloatInt, PathHolder()))
    }

    @Test
    fun `GIVEN from is unsupported type THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(unsupportedType, mapCollectionTypeFloatInt, PathHolder()))
    }

    @Test
    fun `GIVEN to is unsupported collection then false is returned`() {
        Assertions.assertFalse(converter.isSupported(mapCollectionTypeFloatInt, unsupportedType, PathHolder()))
    }

    @Test
    fun `GIVEN from is nullable and to is not nullable THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(mapCollectionTypeFloatIntNullable, mapCollectionTypeFloatInt, PathHolder()))
    }

    @Test
    fun `GIVEN from is not compatible with to value type argument THEN false is returned`() {
        Assertions.assertFalse(converter.isSupported(mapCollectionTypeFloatIntNullableValueArgument, mapCollectionTypeFloatInt, PathHolder()))
    }

    @Test
    fun `GIVEN arguments are all correct THEN true is returned`() {
        Assertions.assertTrue(converter.isSupported(mapCollectionTypeFloatInt, mapCollectionTypeFloatIntNullableValueArgument, PathHolder()))
    }

    @Test
    fun `GIVEN same type THEN generated code is correct`() {
        val parameterSpec = ParameterSpec.builder("it", mapCollectionTypeFloatInt.toTypeName()).build()
        val result = converter.buildConversionStatement(parameterSpec, mapCollectionTypeFloatInt, mapCollectionTypeFloatInt, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals("it", result.code.toString())
    }

    @Test
    fun `GIVEN different argument types WHEN from is not nullable THEN generated code is correct`() {
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
        val parameterSpec = ParameterSpec.builder("it", mapCollectionTypeFloatInt.toTypeName()).build()
        val result = converter.buildConversionStatement(parameterSpec, mapCollectionTypeFloatInt, mapCollectionTypeFloatChar, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  val result = kotlin.collections.HashMap<out kotlin.Float, out kotlin.Char>()
                  for (entry in it) {
                    val (k, v) = entry
                    val newK = k.let {
                      it
                    }
                    val newV = v.let {
                      it
                    }
                    result[newK] = newV
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
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
        val parameterSpec = ParameterSpec.builder("it", mapCollectionTypeFloatCharNullable.toTypeName()).build()
        val result = converter.buildConversionStatement(parameterSpec, mapCollectionTypeFloatIntNullable, mapCollectionTypeFloatCharNullable, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  if (it == null) {
                    return@run null
                  }
                  val result = kotlin.collections.HashMap<out kotlin.Float, out kotlin.Char>()
                  for (entry in it) {
                    val (k, v) = entry
                    val newK = k.let {
                      it
                    }
                    val newV = v.let {
                      it
                    }
                    result[newK] = newV
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
    }

    @Test
    fun `GIVEN different argument types WHEN from value argument is null THEN generated code is correct`() {
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
        val parameterSpec = ParameterSpec.builder("it", mapCollectionTypeFloatIntNullableValueArgument.toTypeName()).build()
        val result = converter.buildConversionStatement(parameterSpec, mapCollectionTypeFloatIntNullableValueArgument, mapCollectionTypeFloatCharNullableValueArgument, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  val result = kotlin.collections.HashMap<out kotlin.Float, out kotlin.Char?>()
                  for (entry in it) {
                    val (k, v) = entry
                    val newK = k.let {
                      it
                    }
                    if (v == null) {
                      result[newK] = null
                      continue
                    }
                    val newV = v.let {
                      it
                    }
                    result[newK] = newV
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
    }

    @Test
    fun `GIVEN different argument types WHEN not external argument is needed THEN generated code is correct`() {
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
        val parameterSpec = ParameterSpec.builder("it", mapCollectionTypeFloatInt.toTypeName()).build()
        val result = converter.buildConversionStatement(parameterSpec, mapCollectionTypeFloatInt, mapCollectionTypeFloatChar, PathHolder())
        Assertions.assertTrue(result.requiresObjectToConvertFrom)
        Assertions.assertEquals(
            """
                run {
                  val result = kotlin.collections.HashMap<out kotlin.Float, out kotlin.Char>()
                  for (entry in it) {
                    val (k, v) = entry
                    val newK = someObject
                    val newV = someObject
                    result[newK] = newV
                  }
                  result
                }
                
            """.trimIndent(),
            result.code.toString()
        )
    }
}