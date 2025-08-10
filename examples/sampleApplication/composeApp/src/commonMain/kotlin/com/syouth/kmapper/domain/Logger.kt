package com.syouth.kmapper.domain

internal interface Logger {
    fun log(tag: String = TAG, message: String)

    class PrintLogger : Logger {
        override fun log(tag: String, message: String) {
            println("$tag -> $message")
        }
    }

    companion object {
        const val TAG: String = "[LOG]"
    }
}