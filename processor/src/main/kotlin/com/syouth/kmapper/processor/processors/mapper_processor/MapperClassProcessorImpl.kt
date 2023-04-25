package com.syouth.kmapper.processor.processors.mapper_processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.syouth.kmapper.processor.base.*
import com.syouth.kmapper.processor.base.data.MapperInformation
import com.syouth.kmapper.processor.convertors.manager.ConvertersManager
import com.syouth.kmapper.processor.injectors.providerInjector
import com.syouth.kmapper.processor.strategies.Constatnts
import com.syouth.kmapper.processor.strategies.VisitNodeStrategy
import com.syouth.kmapper.processor_annotations.Mapper

internal class MapperClassProcessorImpl constructor(
    private val convertersManager: ConvertersManager,
    private val environment: SymbolProcessorEnvironment
) : MapperClassProcessor {
    override fun process(type: KSClassDeclaration) {
        if (type.annotations.find { it.annotationType.checkSame(Mapper::class) } == null) throw IllegalStateException("Should be annotated with @Mapper")
        convertersManager.initializeForMapperClass(type)
        val functionDeclarations = type.findAbstractMappingFunctionDeclarations()
        val mappersToImplement = functionDeclarations.map { it.getMappingInformation() }

        val injector = providerInjector(environment.options)
        val implementationBuilder =
            TypeSpec
                .classBuilder("${type.simpleName.asString()}Impl")
                .addModifiers(injector.classModifier)
                .addSuperinterface(type.toClassName())

        injector.processClassSpec(implementationBuilder)

        for (mapper in mappersToImplement) {
            processMapper(mapper, implementationBuilder)
        }

        val packageName = type.packageName.asString()
        val fileName = "${type.simpleName.asString()}Impl"
        val fileSpec = FileSpec
            .builder(packageName, fileName)
            .addType(implementationBuilder.build())
            .build()
        val dependencyFile = type.containingFile ?: throw IllegalStateException("Mapper should be in a file")
        environment.codeGenerator.createNewFile(
            dependencies = Dependencies(true, dependencyFile),
            packageName,
            fileName,
            "kt"
        )
            .writer()
            .use { fileSpec.writeTo(it) }

    }

    private fun processMapper(mapperInfo: MapperInformation, builder: TypeSpec.Builder) {
        convertersManager.initializeForMapperFunction(mapperInfo.func)
        val pathHolder = PathHolder()
        val from = mapperInfo.from.type.resolve()
        val converter =
            convertersManager.findConverterForTypes(from, mapperInfo.to, pathHolder)
                ?: throw IllegalStateException("Don't know how to map ${mapperInfo.from.name?.asString()} to ${mapperInfo.to.toClassName()}")
        val fromParameterSpec = ParameterSpec.builder(mapperInfo.from.name!!.asString(), mapperInfo.from.type.toTypeName()).build()
        val bundle = Bundle().apply {
            this[Constatnts.VISITED_NODES_LIST] = mutableListOf<String>()
        }
        val converterBlock = converter.buildConversionStatement(
            fromParameterSpec,
            from,
            mapperInfo.to,
            pathHolder,
            bundle
        )
        val funSpecBuilder = FunSpec
            .builder(mapperInfo.func.simpleName.asString())
            .returns(mapperInfo.to.toClassName())
            .addParameter(fromParameterSpec)
            .addParameters(mapperInfo.mapperParams.subList(1, mapperInfo.mapperParams.size).map { ParameterSpec.builder(it.name!!.asString(), it.type.toTypeName()).build() })
            .addModifiers(KModifier.OVERRIDE)
            .addCode("return %L", converterBlock.code)
        builder
            .addFunction(funSpecBuilder.build())
    }
}