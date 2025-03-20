package se.makrohard.jobs.battleship

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import battleship.composeapp.generated.resources.Res
import battleship.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun App() {
    MaterialTheme {
       BattleshipScreen()
    }
}

@Composable
fun BattleshipGrid(size: Int = 10, onCellClick: (Int, Int) -> Unit) {
    val letters = ('A'..'J').toList()
    val numbers = (1..10).toList()

    Column(modifier = Modifier.padding(16.dp)) {
        // Top Header (Letters A-J)
        Row {
            Spacer(modifier = Modifier.size(36.dp)) // Empty corner space
            letters.forEach { letter ->
                Text(
                    text = letter.toString(),
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp),
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }

        // Grid with Number Labels
        numbers.forEachIndexed { rowIndex, number ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Left Number Header
                Text(
                    text = number.toString(),
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp),
                    style = TextStyle(fontSize = 18.sp)
                )

                // Grid Cells
                repeat(size) { colIndex ->
                    GridCell(rowIndex, colIndex, onClick = { onCellClick(rowIndex, colIndex) })
                }
            }
        }
    }
}

@Composable
fun GridCell(x: Int, y: Int, onClick: () -> Unit) {
    var color by remember { mutableStateOf(Color.Gray) }

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(color, RoundedCornerShape(4.dp))
            .clickable {
                color = Color.Blue // Change color on click (can be customized)
                onClick()
            }
            .padding(4.dp)
    )
}

@Composable
fun BattleshipScreen() {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {

            // First player grids
            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()){
                BattleshipGrid { x, y ->
                    println("Clicked on cell (${x + 1}, ${'A' + y})")
                }
                BattleshipGrid { x, y ->
                    println("Clicked on cell (${x + 1}, ${'A' + y})")
                }
            }

            // Second player grids
            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()) {
                BattleshipGrid { x, y ->
                    println("Clicked on cell (${x + 1}, ${'A' + y})")
                }
                BattleshipGrid { x, y ->
                    println("Clicked on cell (${x + 1}, ${'A' + y})")
                }
            }
        }
    }
}