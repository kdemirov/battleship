package se.makrohard.jobs.battleship.model

data class Coordinate (
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

class ShipPlacementException(override val message: String = "There is already ship in that position")
    : RuntimeException(message)