package se.makrohard.jobs.battleship.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.makrohard.jobs.battleship.model.Scoreboard

/**
 * Displays winner and a scoreboard between the two players.
 */
@Composable
fun Winner(
    scoreboard: Scoreboard,
    winner: () -> String,
    reset: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = winner(),
            color = Color.Green,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Player 1: ${scoreboard.player1} : Player 2: ${scoreboard.player2}",
            modifier = Modifier.padding(16.dp), color = Color.Black
        )
        Button(onClick = { reset() }, modifier = Modifier.padding(16.dp)) {
            Text(text = "New Game")
        }
    }
}