package se.makrohard.jobs.battleship.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.makrohard.jobs.battleship.model.CellType
import se.makrohard.jobs.battleship.model.GridType

/**
 * Grid Cell in which the color is dynamically allocated by the grid type,
 * if the GridType is for ships the hidden cell is gray the hit cell is red and the placed
 * cell is blue, if the grid type is for shots the missed cell is white the hit cell is blue and
 * the placed cell is gray.
 */
@Composable
fun GridCell(
    x: Int,
    y: Int,
    gridType: GridType,
    grid: Array<Array<CellType>>,
    onClick: () -> Unit
) {
    val gridCell = grid[x][y]
    val cellColor = when (gridType) {
        GridType.SHIPS -> {
            if (gridCell == CellType.PLACED) Color.Blue
            else if (gridCell == CellType.HIT) Color.Red
            else Color.Gray
        }

        GridType.SHOTS -> {
            if (gridCell == CellType.PLACED) Color.Gray
            else if (gridCell == CellType.HIT) Color.Blue
            else if (gridCell == CellType.MISSED) Color.White
            else Color.Gray
        }
    }
    var color by remember { mutableStateOf(cellColor) }

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(color, RoundedCornerShape(4.dp))
            .clickable {
                color = Color.Cyan
                onClick()
            }
            .padding(4.dp)
    )
}