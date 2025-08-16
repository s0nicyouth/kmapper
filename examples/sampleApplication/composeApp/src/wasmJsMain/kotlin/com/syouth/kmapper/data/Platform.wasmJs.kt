package com.syouth.kmapper.data

internal class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

internal actual fun getPlatform(): Platform = WasmPlatform()