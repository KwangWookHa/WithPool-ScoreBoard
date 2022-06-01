package wook.pool.board.data.model

import com.google.firebase.Timestamp

data class NineBallMatch(
    val gameType: String? = null,
    val adjustment: Int? = null,

    @field:JvmField
    val isLive: Boolean? = false,

    val playerLeftName: String? = null,
    val playerRightName: String? = null,

    val playerLeftScore: Int? = null,
    val playerRightScore: Int? = null,

    val playerLeftRunOut: Int? = null,
    val playerRightRunOut: Int? = null,

    val playerWinnerName: String? = null,
    val playerLoserName: String? = null,

    val matchStartDateTime: Timestamp? = null,
    val matchEndDateTime: Timestamp? = null,
)
