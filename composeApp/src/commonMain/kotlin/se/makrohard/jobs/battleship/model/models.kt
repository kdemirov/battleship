package se.makrohard.jobs.battleship.model

data class Coordinate(
    val x: Int,
    val y: Int
)

data class Ship(
    val coordinate: Coordinate,
    val size: Int,
    var numberOfHits: Int = 0,
    var sunk: Boolean = false
)

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

enum class GridType {
    SHIPS, SHOTS
}

enum class CellType {
    HIDDEN, PLACED, HIT, MISSED
}

data class AvailableShip(
    val name: String,
    val size: Int
)

data class Scoreboard(
    var player1: Int,
    var player2: Int
)

val ships = listOf(
    AvailableShip("Carrier", 5),
    AvailableShip("Battleship", 4),
    AvailableShip("Cruiser", 3),
    AvailableShip("Submarine", 3),
    AvailableShip("Destroyer", 2)
)

class ShipPlacementException(override val message: String = "There is already ship in that position") :
    RuntimeException(message)

class CellAlreadyHitException(override val message: String = "Cell already hit") :
    RuntimeException(message)

class CellAlreadyMissedException(override val message: String = "Cell already missed") :
    RuntimeException(message)

class AllShipArePlacedException(override val message: String = "All ships are placed") :
    RuntimeException(message)

class BattleShipIsAlreadyPlacedException(override val message: String = "Battleship is placed already") :
    RuntimeException(message)

class CarrierShipIsAlreadyPlacedException(override val message: String = "Carrier is placed already") :
    RuntimeException(message)

class CruiserAndSubmarineShipsAreAlreadyPlacedException(override val message: String = "Cruiser and Submarine are placed already") :
    RuntimeException(message)

class DestroyerShipIsAlreadyPlacedException(override val message: String = "Destroyer is placed already") :
    RuntimeException(message)

class ThereIsNoSuchShipWithAllocatedSizeException(
    val size: Int,
    override val message: String = "There is no ship with size: ${size}"
) :
    RuntimeException(message)

class NoShipsPlacedException(override val message: String = "You must first place your ships in order to take a shot") :
    RuntimeException(message)