package se.makrohard.jobs.battleship.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.makrohard.jobs.battleship.model.AllShipArePlacedException
import se.makrohard.jobs.battleship.model.BattleShipIsAlreadyPlacedException
import se.makrohard.jobs.battleship.model.CarrierShipIsAlreadyPlacedException
import se.makrohard.jobs.battleship.model.CellAlreadyHitException
import se.makrohard.jobs.battleship.model.CellAlreadyMissedException
import se.makrohard.jobs.battleship.model.CellType
import se.makrohard.jobs.battleship.model.Coordinate
import se.makrohard.jobs.battleship.model.CruiserAndSubmarineShipsAreAlreadyPlacedException
import se.makrohard.jobs.battleship.model.DestroyerShipIsAlreadyPlacedException
import se.makrohard.jobs.battleship.model.Direction
import se.makrohard.jobs.battleship.model.GridType
import se.makrohard.jobs.battleship.model.NoShipsPlacedException
import se.makrohard.jobs.battleship.model.Ship
import se.makrohard.jobs.battleship.model.ShipPlacementException
import se.makrohard.jobs.battleship.model.ships

/**
 * Displays player's grid one for placement the ships the other one for shooting the enemy ships.
 */
@Composable
fun PlayerGrids(
    shipGrid: Array<Array<CellType>>,
    shotGrid: Array<Array<CellType>>,
    placeShip: (size: Int, startCoordinate: Coordinate, direction: Direction) -> Unit,
    shotShip: (coordinate: Coordinate) -> Unit,
    playerShips: List<Ship>,
    placementFlag: MutableState<Boolean>,
    startCoordinate: MutableState<Coordinate>,
    error: MutableState<String>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        AvailableShips()
        BattleshipGrid(
            grid = shipGrid,
            gridType = GridType.SHIPS
        ) { x, y ->
            try {
                placeShip(placementFlag, startCoordinate, error, placeShip, x, y)
            } catch (o_O: AllShipArePlacedException) {
                error.value = o_O.message
                placementFlag.value = true
            } catch (o_O: ShipPlacementException) {
                error.value = o_O.message
                placementFlag.value = true
            } catch (o_O: CarrierShipIsAlreadyPlacedException) {
                error.value = o_O.message
                placementFlag.value = true
            } catch (o_O: BattleShipIsAlreadyPlacedException) {
                error.value = o_O.message
                placementFlag.value = true
            } catch (o_O: CruiserAndSubmarineShipsAreAlreadyPlacedException) {
                error.value = o_O.message
                placementFlag.value = true
            } catch (o_O: DestroyerShipIsAlreadyPlacedException) {
                error.value = o_O.message
                placementFlag.value = true
            }
        }
        AnimatedVisibility(playerShips.isNotEmpty() && playerShips.size == 5) {
            BattleshipGrid(
                grid = shotGrid,
                gridType = GridType.SHOTS
            ) { x, y ->
                try {
                    shotShip(Coordinate(x, y))
                } catch (o_O: CellAlreadyHitException) {
                    error.value = o_O.message
                } catch (o_O: CellAlreadyMissedException) {
                    error.value = o_O.message
                } catch (o_O: NoShipsPlacedException) {
                    error.value = o_O.message
                }
            }
        }
    }
}

@Composable
fun AvailableShips() {
    Column(
        modifier = Modifier.padding(16.dp)
            .border(3.dp, Color.Black)
    ) {
        ships.forEach {
            Text(
                "Name: ${it.name} Size: ${it.size}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

private fun placeShip(
    placementFlagState: MutableState<Boolean>,
    startCoordinateState: MutableState<Coordinate>,
    error: MutableState<String>,
    placeShip: (size: Int, startCoordinate: Coordinate, direction: Direction) -> Unit,
    x: Int,
    y: Int
) {
    var placementFlag = placementFlagState.value
    var startCoordinate = startCoordinateState.value
    if (placementFlag) {
        startCoordinateState.value = Coordinate(x, y)
        placementFlagState.value = false
    } else {
        var size: Int
        if (startCoordinate.x == x
            && startCoordinate.y >= y
        ) {
            size = (startCoordinate.y + 1) - y
            placeShip(size, startCoordinate, Direction.LEFT)
        } else if (startCoordinate.x == x &&
            startCoordinate.y <= y
        ) {
            size = (y + 1) - startCoordinate.y
            placeShip(size, startCoordinate, Direction.RIGHT)
        } else if (startCoordinate.x <= x &&
            startCoordinate.y == y
        ) {
            size = (x + 1) - startCoordinate.x
            placeShip(size, startCoordinate, Direction.DOWN)
        } else if (startCoordinate.x >= x &&
            startCoordinate.y == y
        ) {
            size = (startCoordinate.x + 1) - x
            placeShip(size, startCoordinate, Direction.UP)
        }
        placementFlagState.value = true
        error.value = ""
    }
}