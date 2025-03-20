import se.makrohard.jobs.battleship.BattleshipViewModel
import se.makrohard.jobs.battleship.model.Coordinate
import se.makrohard.jobs.battleship.model.Direction
import se.makrohard.jobs.battleship.model.ShipPlacementException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BattleShipViewModelTest {
    val viewModel = BattleshipViewModel()

    @Test
    fun shouldTestPlacementOfAShip() {
        viewModel.placeShip(5, Coordinate(0, 0), Direction.LEFT)

        assertEquals(5, viewModel.player1Ships[0].size)
        assertEquals(Coordinate(0,0), viewModel.player1Ships[0].coordinate)
    }

    @Test
    fun shouldThrowExceptionOnPlacementOfAShip() {
        viewModel.placeShip(5, Coordinate(0, 0), Direction.LEFT)
        // player 2 sets ship
        viewModel.placeShip(5, Coordinate(1,1), Direction.LEFT)
        assertFailsWith(ShipPlacementException::class) {
            // player 1 sets ship
            viewModel.placeShip(3, Coordinate(0,0), Direction.DOWN)
        }
    }

    @Test
    fun shouldShotShip() {
        // player 1 sets ship
        viewModel.placeShip(5, Coordinate(0, 0), Direction.LEFT)

        // player 2 shots a ship
        val result = viewModel.shot(Coordinate(4, 0))

        assertEquals(true, result)
        assertEquals(1, viewModel.player1Ships[0].numberOfHits)
    }

    @Test
    fun shouldMissShip() {
        // player 1 sets ship
        viewModel.placeShip(5, Coordinate(0, 0), Direction.LEFT)

        // player 2 shots a ship
        val result = viewModel.shot(Coordinate(5, 0))

        assertEquals(false, result)
    }

    @Test
    fun shouldSunkShip() {
        // player 1 sets ship
        viewModel.placeShip(1, Coordinate(0, 0), Direction.LEFT)

        // player 2 shots a ship
        val result = viewModel.shot(Coordinate(0,0))

        assertEquals(true, result)
        assertEquals(true, viewModel.player1Ships[0].sunk)
    }
}