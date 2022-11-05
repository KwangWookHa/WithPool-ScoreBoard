package wook.pool.board

object Constant {

    const val IS_NOT_INITIALIZED = -9999

    const val DURATION_ITEM_ANIMATION = 200L

    const val GUEST = "Guest"

    object Collection {
        const val COLLECTION_NINE_BALL_MATCH = "nine_ball_match"
        const val COLLECTION_PLAYERS = "players"
        const val COLLECTION_APP_VERSION = "app_version"
        const val COLLECTION_COUNT = "count"
    }

    object Document {
        const val DOCUMENT_SCORE_BOARD = "score_board"
    }

    object Field {
        const val FIELD_COUNT = "count"
        const val FIELD_NAME = "name"
        const val FILED_IS_LIVE = "isLive"
        const val FILED_PLAYER_LEFT_NAME = "playerLeftName"
        const val FILED_PLAYER_RIGHT_NAME = "playerRightName"
        const val FILED_PLAYER_LEFT_RUN_OUT = "playerLeftRunOut"
        const val FILED_PLAYER_RIGHT_RUN_OUT = "playerRightRunOut"
        const val FILED_PLAYER_LEFT_SCORE = "playerLeftScore"
        const val FILED_PLAYER_RIGHT_SCORE = "playerRightScore"
        const val FILED_PLAYER_WINNER_NAME = "playerWinnerName"
        const val FILED_PLAYER_LOSER_NAME = "playerLoserName"
        const val FILED_END_TIME_STAMP = "matchEndTimeStamp"
    }


}