package wook.withpool.board.screen.scoreboard

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import wook.withpool.board.R
import wook.withpool.board.base.BaseActivity
import wook.withpool.board.databinding.ActivityScoreBoardBinding


class ScoreBoardActivity: BaseActivity() {

    private var _binding : ActivityScoreBoardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_score_board)
    }
}