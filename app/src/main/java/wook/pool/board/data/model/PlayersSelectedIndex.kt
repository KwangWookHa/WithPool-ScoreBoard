package wook.pool.board.data.model

enum class PlayersSelectedIndex(val index: Int, val handicapLeft: Int, val handicapRight: Int) {

    INDEX_HANDICAP_3_4(0,3,4),
    INDEX_HANDICAP_5_6(1,5,6),
    INDEX_HANDICAP_7_8(2,7,8),
    INDEX_HANDICAP_9_10(3,9,10),

}