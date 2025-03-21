package se.makrohard.jobs.battleship

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import se.makrohard.jobs.battleship.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Battleship",
    ) {
        App()
    }
}