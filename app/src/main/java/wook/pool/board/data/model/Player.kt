package wook.pool.board.data.model

import java.io.Serializable

data class Player(
    val club: String? = null,
    val name: String? = null,
    val handicap: Int? = null,
) : Serializable