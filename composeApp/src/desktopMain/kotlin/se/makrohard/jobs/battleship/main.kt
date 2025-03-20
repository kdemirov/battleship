package se.makrohard.jobs.battleship

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Battleship",
    ) {
        App()
    }
}