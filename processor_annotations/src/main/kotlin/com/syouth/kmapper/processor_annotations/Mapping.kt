package com.syouth.kmapper.processor_annotations

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Mapping(val target: String, val source: String)
