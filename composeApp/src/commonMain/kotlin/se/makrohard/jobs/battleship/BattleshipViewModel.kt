package se.makrohard.jobs.battleship

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import se.makrohard.jobs.battleship.model.Coordinate
import se.makrohard.jobs.battleship.model.Direction
import se.makrohard.jobs.battleship.model.Ship
import se.makrohard.jobs.battleship.model.ShipPlacementException

class BattleshipViewModel : ViewModel() {
    private val _player1Grid: MutableState<Array<Array<Int>>> = mutableStateOf(arrayOf(arrayOf()))
    private val _player2Grid: MutableState<Array<Array<Int>>> = mutableStateOf(arrayOf(arrayOf()))
    private val _player1Ship: MutableState<MutableList<Ship>> = mutableStateOf(mutableListOf())
    private val _player2Ship: MutableState<MutableList<Ship>> = mutableStateOf(mutableListOf())
    private val _playerTurn: MutableState<Boolean> = mutableStateOf(true)

    val player1Ships = _player1Ship.value

    init {
        _player1Grid.value = Array(10) {
            Array(10) {
                0
            }
        }
        _player2Grid.value = Array(10) {
            Array(10) {
                0
            }
        }
    }

    fun placeShip(size: Int, coordinate: Coordinate, direction: Direction) {
        if (_playerTurn.value) {
            placeShipInGrid(size, coordinate, direction, _player1Grid.value)
            _player1Ship.value.add(Ship(coordinate, size))
            // set player 2 turn
            _playerTurn.value = false
        } else {
            placeShipInGrid(size, coordinate, direction, _player2Grid.value)
            _player2Ship.value.add(Ship(coordinate, size))
            // set player 1 turn
            _playerTurn.value = true
        }
    }

    fun shot(coordinate: Coordinate): Boolean {
        var hit: Boolean
        if (_playerTurn.value) {
            hit = takeShotAtShip(_player2Grid.value, _player2Ship.value, coordinate)
            _playerTurn.value = false
        } else {
            hit = takeShotAtShip(_player1Grid.value, _player1Ship.value, coordinate)
            _playerTurn.value = true
        }
        return hit
    }

    private fun takeShotAtShip(
        playerGrid: Array<Array<Int>>,
        playerShips: List<Ship>,
        coordinate: Coordinate
    ): Boolean {
        if (playerGrid[coordinate.x][coordinate.y] == 1) {
            val ship = findShip(coordinate, playerShips)
            ship.let {
                ship!!.numberOfHits = ++ship.numberOfHits
                if (ship.numberOfHits == ship.size) {
                    ship.sunk = true
                }
            }
            return true
        } else {
            return false
        }
    }

    private fun findShip(coordinate: Coordinate, playerShips: List<Ship>): Ship? {
        return playerShips.find {
            coordinate.x <= it.coordinate.x + it.size ||
                    coordinate.x >= it.coordinate.x - it.size ||
                    coordinate.y <= it.coordinate.y + it.size ||
                    coordinate.y >= it.coordinate.y - it.size
        }
    }

    private fun placeShipInGrid(
        size: Int,
        coordinate: Coordinate,
        direction: Direction,
        grid: Array<Array<Int>>
    ) {
        when (direction) {
            Direction.LEFT -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x + i][coordinate.y] == 1) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x + i][coordinate.y] = 1
                    }
                }
            }

            Direction.RIGHT -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x - i][coordinate.y] == 1) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x - i][coordinate.y] = 1
                    }
                }
            }

            Direction.UP -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x][coordinate.y -i] == 1) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x][coordinate.y - i]
                    }
                }
            }

            Direction.DOWN -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x][coordinate.y + i] == 1) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x][coordinate.y + i]
                    }
                }
            }
        }
    }
}