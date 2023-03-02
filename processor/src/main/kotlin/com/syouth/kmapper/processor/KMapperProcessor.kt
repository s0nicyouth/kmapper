package com.syouth.kmapper.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.syouth.kmapper.processor.base.findAnnotations
import com.syouth.kmapper.processor.convertors.*
import com.syouth.kmapper.processor.convertors.manager.ConvertersManagerImpl
import com.syouth.kmapper.processor.processors.mapper_processor.MapperClassProcessorImpl
import com.syouth.kmapper.processor_annotations.Mapper

internal class KMapperProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedClasses = resolver.findAnnotations<KSClassDeclaration>(Mapper::class)
        val builtInConverters = mutableListOf<TypeConvertor>()
        val convertersManager = ConvertersManagerImpl(builtInConverters)
        builtInConverters.add(NonCollectionTypeConverter())
        builtInConverters.add(PojoClassTypeConverter(convertersManager))
        builtInConverters.add(CollectionTypeConverter(convertersManager))
        builtInConverters.add(MapCollectionConverter(convertersManager))

        val mapperClassProcessor = MapperClassProcessorImpl(
            convertersManager,
            environment
        )
        annotatedClasses.forEach(mapperClassProcessor::process)
        return emptyList()
    }
}
