package se.makrohard.jobs.battleship.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import se.makrohard.jobs.battleship.BattleshipViewModel
import se.makrohard.jobs.battleship.model.Coordinate

/**\
 * Battleship game screen in which are shown the available ships for allocation and
 * the player grids one for placement ot the ships the other for shooting the enemy ship.
 */
@Composable
fun BattleshipScreen(viewModel: BattleshipViewModel) {
    var placementFlag = mutableStateOf(true)
    var startCoordinate = mutableStateOf(Coordinate(0, 0))
    var error = remember { mutableStateOf("") }
    val playerTurn = viewModel.playerTurn
    val isGameOver = viewModel.isGameOver
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {

            AnimatedVisibility(!isGameOver.value) {
                // First player grids
                AnimatedVisibility(playerTurn.value) {
                    PlayerGrids(
                        viewModel.player1Grid.value,
                        viewModel.player2Grid.value,
                        viewModel::placeShip,
                        viewModel::shot,
                        viewModel.player2Ships.value,
                        placementFlag,
                        startCoordinate,
                        error
                    )

                }

                // Second player grids
                AnimatedVisibility(!playerTurn.value) {
                    PlayerGrids(
                        viewModel.player2Grid.value,
                        viewModel.player1Grid.value,
                        viewModel::placeShip,
                        viewModel::shot,
                        viewModel.player1Ships.value,
                        placementFlag,
                        startCoordinate,
                        error
                    )
                }
            }


            AnimatedVisibility(
                error.value.isNotEmpty(),
                enter = slideInVertically(animationSpec = tween(durationMillis = 1000))
                        + expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Text(
                    text = error.value,
                    color = Color.Red,
                )
            }
        }
        AnimatedVisibility(isGameOver.value) {
            Winner(viewModel.scoreboard, viewModel::winner, viewModel::reset)
        }
    }
}