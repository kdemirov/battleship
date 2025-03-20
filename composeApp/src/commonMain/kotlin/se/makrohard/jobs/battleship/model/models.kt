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