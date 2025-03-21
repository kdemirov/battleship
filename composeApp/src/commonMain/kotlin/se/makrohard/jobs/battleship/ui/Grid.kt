package se.makrohard.jobs.battleship.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.makrohard.jobs.battleship.model.CellType
import se.makrohard.jobs.battleship.model.GridType

/**
 * Player's grid 10x10.
 */
@Composable
fun BattleshipGrid(
    grid: Array<Array<CellType>>,
    gridType: GridType,
    onCellClick: (Int, Int) -> Unit
) {
    val letters = ('A'..'J').toList()
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
        grid.forEachIndexed { x, row ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Left Number Header
                Text(
                    text = (x + 1).toString(),
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp),
                    style = TextStyle(fontSize = 18.sp)
                )

                // Grid Cells
                grid[x].forEachIndexed { y, col ->
                    GridCell(x, y, gridType, grid) {
                        onCellClick(x, y)
                    }
                }
            }
        }
    }
}