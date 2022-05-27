package wook.pool.board.data.model

data class Match(
    val gameType: String? = null,
    val isLive: Boolean? = false,
    val playerLeftName: String? = null,
    val playerRightName: String? = null,
    val playerLeftScore: Int? = null,
    val playerRightScore: Int? = null,
    val playerLeftPoint: Int? = null,
    val playerRightPoint: Int? = null,
    val playerLeftTurnCount: Int? = null,
    val playerRightTurnCount: Int? = null,
    val playerWinnerPlayerName: String? = null,
    val matchStartTimeStamp: String? = null,
    val matchEndTimeStamp: String? = null,
)
