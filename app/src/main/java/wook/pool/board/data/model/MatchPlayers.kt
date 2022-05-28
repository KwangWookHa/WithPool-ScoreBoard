package wook.pool.board.data.model

import java.io.Serializable

data class MatchPlayers(
    val playerLeft: Player,
    val playerLeftAdjustedHandicap: Int,
    val playerRight: Player,
    val playerRightAdjustedHandicap: Int,
) : Serializable