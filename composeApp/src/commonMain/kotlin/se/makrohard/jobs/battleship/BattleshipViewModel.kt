package se.makrohard.jobs.battleship

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
import se.makrohard.jobs.battleship.model.NoShipsPlacedException
import se.makrohard.jobs.battleship.model.Scoreboard
import se.makrohard.jobs.battleship.model.Ship
import se.makrohard.jobs.battleship.model.ShipPlacementException
import se.makrohard.jobs.battleship.model.ThereIsNoSuchShipWithAllocatedSizeException

/**
 * Battleship game ViewModel
 */
class BattleshipViewModel : ViewModel() {

    companion object {
        const val PLAYER1_WIN = "Player 1 Wins"
        const val PLAYER2_WIN = "Player 2 Wins"
        const val NUMBER_OF_ALLOWED_SHIPS = 5
        const val CARRIER_SIZE = 5
        const val BATTLESHIP_SIZE = 4
        const val SUBMARINE_CRUISER_SIZE = 3
        const val DESTROYER_SIZE = 2
        const val SUM_OF_HIT_SHIPS =
            CARRIER_SIZE + BATTLESHIP_SIZE + SUBMARINE_CRUISER_SIZE * 2 + DESTROYER_SIZE
    }

    private val _player1Grid: MutableState<Array<Array<CellType>>> =
        mutableStateOf(fillDefaultGridValues())
    private val _player2Grid: MutableState<Array<Array<CellType>>> =
        mutableStateOf(fillDefaultGridValues())
    private val _player1Ships: MutableState<MutableList<Ship>> =
        mutableStateOf(mutableListOf())
    private val _player2Ships: MutableState<MutableList<Ship>> =
        mutableStateOf(mutableListOf())
    private val _playerTurn: MutableState<Boolean> = mutableStateOf(true)
    private val _isGameOver: MutableState<Boolean> = mutableStateOf(false)
    private val _scoreboard: MutableState<Scoreboard> = mutableStateOf(Scoreboard(0, 0))

    val player1Grid = _player1Grid
    val player2Grid = _player2Grid
    val player1Ships = _player1Ships
    val player2Ships = _player2Ships
    val playerTurn = _playerTurn
    val isGameOver = _isGameOver
    val scoreboard = _scoreboard.value

    /**
     * Places a ship with size with start coordinate from given direction.
     *
     * @param size size of the ship
     * @param coordinate start coordinate for placement
     * @param direction direction in which the placement should be
     */
    fun placeShip(size: Int, coordinate: Coordinate, direction: Direction) {
        if (_playerTurn.value) {
            placeShip(size, coordinate, direction, _player1Ships.value, _player1Grid.value)
        } else {
            placeShip(size, coordinate, direction, _player2Ships.value, _player2Grid.value)
        }
    }

    /**
     * Shots a ship on a given coordinate.
     *
     * @param coordinate given coordinate.
     *
     * @return true if ship is hit otherwise false
     */
    fun shot(coordinate: Coordinate): Boolean {
        var hit: Boolean
        if (_playerTurn.value) {
            hit = takeShotAtShip(_player2Grid.value, _player1Ships.value, coordinate)
            _playerTurn.value = false
        } else {
            hit = takeShotAtShip(_player1Grid.value, _player2Ships.value, coordinate)
            _playerTurn.value = true
        }
        gameOver()
        return hit
    }

    /**
     * Checks if a game is finished, are all ships hit.
     */
    fun gameOver() {
        if (areAllShipsHit(_player1Grid.value) || areAllShipsHit(_player2Grid.value)) {
            _isGameOver.value = true
        }
    }

    /**
     * Checks who is the winner of the game
     *
     * @return winner player 1 or player 2
     */
    fun winner(): String {
        val winner = if (areAllShipsHit(_player1Grid.value)) {
            _scoreboard.value = Scoreboard(_scoreboard.value.player1, ++_scoreboard.value.player2)
            PLAYER2_WIN
        } else {
            _scoreboard.value = Scoreboard(++_scoreboard.value.player1, _scoreboard.value.player2)
            PLAYER1_WIN
        }
        return winner
    }

    fun reset() {
        _player1Grid.value = fillDefaultGridValues()
        _player2Grid.value = fillDefaultGridValues()
        _playerTurn.value = true
        _player1Ships.value = mutableListOf()
        _player2Ships.value = mutableListOf()
        _isGameOver.value = false
    }

    private fun placeShip(
        size: Int, coordinate: Coordinate, direction: Direction,
        ships: MutableList<Ship>, grid: Array<Array<CellType>>
    ) {
        if (size > CARRIER_SIZE && size < DESTROYER_SIZE) {
            throw ThereIsNoSuchShipWithAllocatedSizeException(size)
        }
        checkForDuplicateShips(ships, size)
        if (ships.size == NUMBER_OF_ALLOWED_SHIPS) {
            throw AllShipArePlacedException()
        }
        placeShipInGrid(size, coordinate, direction, grid)
        ships.add(Ship(coordinate, size))

        if (ships.size == NUMBER_OF_ALLOWED_SHIPS) {
            _playerTurn.value = !_playerTurn.value
        }
    }

    private fun checkForDuplicateShips(ships: List<Ship>, size: Int) {
        if (ships.count { it.size == BATTLESHIP_SIZE } == 1 && size == BATTLESHIP_SIZE) {
            throw BattleShipIsAlreadyPlacedException()
        }
        if (ships.count { it.size == CARRIER_SIZE } == 1 && size == CARRIER_SIZE) {
            throw CarrierShipIsAlreadyPlacedException()
        }
        if (ships.count { it.size == SUBMARINE_CRUISER_SIZE } == 2 && size == SUBMARINE_CRUISER_SIZE) {
            throw CruiserAndSubmarineShipsAreAlreadyPlacedException()
        }
        if (ships.count { it.size == DESTROYER_SIZE } == 1 && size == DESTROYER_SIZE) {
            throw DestroyerShipIsAlreadyPlacedException()
        }
    }

    private fun areAllShipsHit(grid: Array<Array<CellType>>): Boolean {
        return grid.flatMap { it.map { it }.filter { it == CellType.HIT } }
            .count() == SUM_OF_HIT_SHIPS
    }

    private fun takeShotAtShip(
        playerGrid: Array<Array<CellType>>,
        ships: List<Ship>,
        coordinate: Coordinate
    ): Boolean {
        if (ships.size < NUMBER_OF_ALLOWED_SHIPS) {
            throw NoShipsPlacedException()
        }
        if (playerGrid[coordinate.x][coordinate.y] == CellType.HIT) {
            throw CellAlreadyHitException()
        }
        if (playerGrid[coordinate.x][coordinate.y] == CellType.MISSED) {
            throw CellAlreadyMissedException()
        }
        if (playerGrid[coordinate.x][coordinate.y] == CellType.PLACED) {
            playerGrid[coordinate.x][coordinate.y] = CellType.HIT
            return true
        } else {
            playerGrid[coordinate.x][coordinate.y] = CellType.MISSED
            return false
        }
    }

    private fun placeShipInGrid(
        size: Int,
        coordinate: Coordinate,
        direction: Direction,
        grid: Array<Array<CellType>>
    ) {
        when (direction) {
            Direction.LEFT -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x][coordinate.y - i] == CellType.PLACED) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x][coordinate.y - i] = CellType.PLACED
                    }
                }
            }

            Direction.RIGHT -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x][coordinate.y + i] == CellType.PLACED) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x][coordinate.y + i] = CellType.PLACED
                    }
                }
            }

            Direction.UP -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x - i][coordinate.y] == CellType.PLACED) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x - i][coordinate.y] = CellType.PLACED
                    }
                }
            }

            Direction.DOWN -> {
                for (i in 0..size - 1) {
                    if (grid[coordinate.x + i][coordinate.y] == CellType.PLACED) {
                        throw ShipPlacementException()
                    } else {
                        grid[coordinate.x + i][coordinate.y] = CellType.PLACED
                    }
                }
            }
        }
    }

    private fun fillDefaultGridValues(): Array<Array<CellType>> {
        return Array(10) {
            Array(10) {
                CellType.HIDDEN
            }
        }
    }
}
