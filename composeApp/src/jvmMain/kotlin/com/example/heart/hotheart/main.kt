package com.example.heart.hotheart

import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition.Aligned(Alignment.Center),
    )
    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "hotheart",
        resizable = false,
        alwaysOnTop = true,
        onPreviewKeyEvent = {
            when (it.key) {
                Key.Escape -> exitApplication()
            }
            true
        },
        undecorated = true,
        transparent = true,
    ) {
        App()
    }
}