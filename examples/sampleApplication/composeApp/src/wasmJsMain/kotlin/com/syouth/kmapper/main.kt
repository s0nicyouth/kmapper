package com.syouth.kmapper

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.syouth.kmapper.presentation.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
internal fun main() {
    ComposeViewport(document.body!!) {
        App()
    }
}