package com.syouth.kmapper.processor.base

internal class Bundle {
    private val map: MutableMap<String, Any?> = mutableMapOf()

    operator fun<T> get(key: String): T? = map[key] as T?

    operator fun<T> set(key: String, value: T?) {
        map[key] = value
    }
}