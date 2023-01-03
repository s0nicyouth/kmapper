package com.syouth.kmapper.processor.injectors

internal fun providerInjector(options: Map<String, String>): Injector = when(options["injector"]?.lowercase()) {
    "koin" -> KoinInjector(options)
    else -> EmptyInjector()
}