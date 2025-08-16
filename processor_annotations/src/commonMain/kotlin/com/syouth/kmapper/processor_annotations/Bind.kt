package com.syouth.kmapper.processor_annotations

/**
 * Binds provided value parameter of mapper function to
 * corresponding property of resulting class.
 * @param to if empty will bind to property with the same name as parameter,
 * otherwise will bind to property with provided name
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class Bind(val to: String = "")
