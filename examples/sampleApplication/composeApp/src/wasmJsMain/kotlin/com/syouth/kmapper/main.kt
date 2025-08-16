package com.syouth.kmapper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.ComposeViewport
import com.syouth.kmapper.presentation.App
import kmapper.examples.sampleapplication.composeapp.generated.resources.NotoColorEmoji
import kmapper.examples.sampleapplication.composeapp.generated.resources.Res
import kotlinx.browser.document
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
internal fun main() {

    ComposeViewport(document.body!!) {
        val emojiFont = preloadFont(Res.font.NotoColorEmoji).value
        var fontsFallbackInitialiazed by remember { mutableStateOf(false) }

        if (emojiFont != null && fontsFallbackInitialiazed) {
            println("Fonts are ready")
            App()
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.DarkGray.copy(alpha = 0.8f))
                    .clickable { }) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            println("Fonts are not ready yet")
        }

        val fontFamilyResolver = LocalFontFamilyResolver.current
        LaunchedEffect(fontFamilyResolver, emojiFont) {
            if (emojiFont != null) {
                // we have an emoji on Strings tab
                fontFamilyResolver.preload(FontFamily(listOf(emojiFont)))
                fontsFallbackInitialiazed = true
            }
        }
    }
}