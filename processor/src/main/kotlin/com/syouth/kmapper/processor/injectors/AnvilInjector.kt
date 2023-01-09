package com.syouth.kmapper.processor.injectors

import com.squareup.kotlinpoet.*

internal class AnvilInjector(options: Map<String, String>): Injector {
    private val bindingScope: ClassName = options["anvilBindingScope"]?.let { extractClassName(it) } ?: error("You have to provide anvilBindingScope")
    private val generateAsSingletons: Boolean = options["anvilGenerateAsSingletons"]?.toBooleanStrictOrNull() ?: false
    override val classModifier: KModifier = KModifier.PUBLIC // this is unfortunate limitation of Anvil

    override fun processClassSpec(builder: TypeSpec.Builder) {
        builder
            .run {
                if (generateAsSingletons){
                  addAnnotation(singletonClassName)
                }else{
                    this
                }
            }
            .addAnnotation(AnnotationSpec.builder(contributesBindingClassName)
            .addMember("scope = %T::class", bindingScope)
            .build())
            .primaryConstructor(FunSpec.constructorBuilder()
                .addAnnotation(injectClassName)
                .build())
    }

    private fun extractClassName(scopeName: String): ClassName{
        val names = scopeName.split(".")
        return ClassName(names.dropLast(1).joinToString("."), names.last())
    }

    companion object {
        private val contributesBindingClassName = ClassName("com.squareup.anvil.annotations", "ContributesBinding")
        private val injectClassName = ClassName("javax.inject", "Inject")
        private val singletonClassName = ClassName("javax.inject", "Singleton")
    }
}