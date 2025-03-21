import se.makrohard.jobs.battleship.BattleshipViewModel
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
import se.makrohard.jobs.battleship.model.ShipPlacementException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Battleship view model test.
 */
class BattleShipViewModelTest {
    val viewModel = BattleshipViewModel()

    @Test
    fun shouldTestPlacementOfAShip() {
        viewModel.placeShip(5, Coordinate(0, 0), Direction.RIGHT)

        assertEquals(5, viewModel.player1Ships.value[0].size)
        assertEquals(Coordinate(0, 0), viewModel.player1Ships.value[0].coordinate)
        assertEquals(CellType.PLACED, viewModel.player1Grid.value[0][0])
        assertEquals(CellType.PLACED, viewModel.player1Grid.value[0][1])
        assertEquals(CellType.PLACED, viewModel.player1Grid.value[0][2])
        assertEquals(CellType.PLACED, viewModel.player1Grid.value[0][3])
        assertEquals(CellType.PLACED, viewModel.player1Grid.value[0][4])
    }

    @Test
    fun shouldThrowExceptionOnPlacementOfAShip() {
        viewModel.placeShip(5, Coordinate(0, 0), Direction.RIGHT)

        assertFailsWith(ShipPlacementException::class) {
            // player 1 sets ship
            viewModel.placeShip(3, Coordinate(0, 0), Direction.DOWN)
        }
    }

    @Test
    fun shouldThrowNoShipsPlacedException() {
        // player 1 sets ship
        viewModel.placeShip(5, Coordinate(0, 0), Direction.RIGHT)

        // player 2 shots a ship
        assertFailsWith(NoShipsPlacedException::class) {
            viewModel.shot(Coordinate(0, 4))
        }
    }

    @Test
    fun shouldMissShip() {
        // player 1 sets ships
        placeShips()

        // player 2 sets ships
        placeShips()

        // player 1 shots a ship
        val result = viewModel.shot(Coordinate(5, 0))

        assertEquals(false, result)
        assertEquals(CellType.MISSED, viewModel.player2Grid.value[5][0])
    }

    @Test
    fun shouldHitShip() {
        // player 1 sets ship
        placeShips()

        // player 2 sets ships
        placeShips()

        // player 1 shots a ship
        val result = viewModel.shot(Coordinate(0, 0))


        assertEquals(true, result)
        assertEquals(CellType.HIT, viewModel.player2Grid.value[0][0])
    }

    @Test
    fun shouldThrowCarrierAlreadyExistsException() {
        // player 1 sets ships
        placeShips()

        // player 2 sets ships
        placeShips()

        assertFailsWith(CarrierShipIsAlreadyPlacedException::class) {
            viewModel.placeShip(5, Coordinate(5, 0), Direction.LEFT)
        }
    }

    @Test
    fun shouldThrowBattleshipAlreadyExistsException() {
        // player 1 sets ships
        placeShips()

        // player 2 sets ships
        placeShips()

        assertFailsWith(BattleShipIsAlreadyPlacedException::class) {
            viewModel.placeShip(4, Coordinate(5, 0), Direction.LEFT)
        }
    }

    @Test
    fun shouldThrowCruiserAndSubmarineAlreadyExistsException() {
        // player 1 sets ships
        placeShips()

        // player 2 sets ships
        placeShips()

        assertFailsWith(CruiserAndSubmarineShipsAreAlreadyPlacedException::class) {
            viewModel.placeShip(3, Coordinate(5, 0), Direction.LEFT)
        }
    }

    @Test
    fun shouldThrowDestroyerAlreadyExistsException() {
        // player 1 sets ships
        placeShips()

        // player 2 sets ships
        placeShips()

        assertFailsWith(DestroyerShipIsAlreadyPlacedException::class) {
            viewModel.placeShip(2, Coordinate(5, 0), Direction.LEFT)
        }
    }

    @Test
    fun shouldThrowCellAlreadyHitException() {
        // player 1 sets ship
        placeShips()

        // player 2 sets ships
        placeShips()

        // player 1 shots a ship
        viewModel.shot(Coordinate(0, 0))
        // player 2 shots a ship
        viewModel.shot(Coordinate(0, 0))

        assertFailsWith(CellAlreadyHitException::class) {
            viewModel.shot(Coordinate(0, 0))
        }
    }

    @Test
    fun shouldThrowCellAlreadyMissedException() {
        // player 1 sets ships
        placeShips()

        // player 2 sets ships
        placeShips()

        // player 1 shots a ship
        viewModel.shot(Coordinate(5, 0))
        viewModel.shot(Coordinate(0, 0))
        assertFailsWith(CellAlreadyMissedException::class) {
            viewModel.shot(Coordinate(5, 0))
        }
    }

    private fun placeShips() {
        viewModel.placeShip(5, Coordinate(0, 0), Direction.RIGHT)
        viewModel.placeShip(4, Coordinate(1, 0), Direction.RIGHT)
        viewModel.placeShip(3, Coordinate(2, 0), Direction.RIGHT)
        viewModel.placeShip(3, Coordinate(3, 0), Direction.RIGHT)
        viewModel.placeShip(2, Coordinate(4, 0), Direction.RIGHT)
    }
}