package com.syouth.kmapper.processor.convertors.manager

import com.google.devtools.ksp.symbol.Nullability
import com.syouth.kmapper.processor.base.PathHolder
import com.syouth.kmapper.processor.testutils.*
import com.syouth.kmapper.processor_annotations.Mapping
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ConverterManagerImplTest {

    private val manager = ConvertersManagerImpl(emptyList())

    @Test
    fun `GIVEN provided mapper class THEN user defined converters initialized correctly`() {
        val returnType = mockKSType(packageName = "kotlin", qualifiedName = "Float", nullability = Nullability.NULLABLE)
        val paramType = mockKSType(packageName = "kotlin", qualifiedName = "Int", nullability = Nullability.NULLABLE)
        val function = mockKSFunctionDeclaration(
            name = "mapIntToFloat",
            returnType = returnType,
            valueParameters = listOf(mockKValueParameter(nameString = "i", type = paramType))
        )
        val func = mockKSClassDeclaration(functions = listOf(function))
        manager.initializeForMapperClass(func)
        val converter = manager.findConverterForTypes(
            paramType,
            returnType.makeNotNullable(),
            PathHolder()
        )
        Assertions.assertNotNull(converter)
    }

    @Test
    fun `GIVEN provided property conversion annotation THEN converter initialized correctly`() {
        val annotation = mockKSAnnotation(
            shortName = Mapping::class.simpleName!!,
            qualifiedName = Mapping::class.qualifiedName!!,
            arguments = listOf(
                mockKSValueArgument("target", "someTarget"),
                mockKSValueArgument("source", "someSource")
            )
        )
        val returnType = mockKSType(declarationProperties = listOf(mockKSProperty("someTarget", mockKSType())))
        val sourceType = mockKValueParameter("someSource", mockKSType())
        val func = mockKSFunctionDeclaration(
            returnType = returnType,
            valueParameters = listOf(sourceType),
            annotations = listOf(annotation)
        )
        manager.initializeForMapperFunction(func)
        Assertions.assertNotNull(manager.propertyConverters.firstOrNull())
    }
}