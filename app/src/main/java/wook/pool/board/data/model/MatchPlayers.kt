package wook.pool.board.data.model

import java.io.Serializable

data class MatchPlayers(
    val playerLeft: Player,
    val playerRight: Player,
    val adjustment: Int,
) : Serializable