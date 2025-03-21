package se.makrohard.jobs.battleship.ui


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import se.makrohard.jobs.battleship.BattleshipViewModel

/**
 * Parent view
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        BattleshipScreen(viewModel = viewModel { BattleshipViewModel() })
    }
}