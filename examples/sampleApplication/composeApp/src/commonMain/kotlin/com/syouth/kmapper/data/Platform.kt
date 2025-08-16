package com.syouth.kmapper.data

internal interface Platform {
    val name: String
}

internal expect fun getPlatform(): Platform
