package com.syouth.kmapper.data.models

internal fun interface Creator<out T : Any> {
    fun create(): T
}