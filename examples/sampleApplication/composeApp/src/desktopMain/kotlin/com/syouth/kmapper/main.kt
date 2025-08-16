package com.syouth.kmapper

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.syouth.kmapper.presentation.App

internal fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMapper Desktop Application",
    ) {
        App()
    }
}