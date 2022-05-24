package wook.pool.board.screen.scoreboard

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import wook.pool.board.R
import wook.pool.board.base.BaseActivity
import wook.pool.board.databinding.ActivityScoreBoardBinding


@AndroidEntryPoint
class ScoreBoardActivity : BaseActivity() {

    private var _binding: ActivityScoreBoardBinding? = null

    private val scoreBoardViewModel: ScoreBoardViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerScoreBoard) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_score_board)
        initObserver()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun initObserver() {
        with(scoreBoardViewModel) {
            screenAction.observe(this@ScoreBoardActivity) {
                if (it.second != null) {
                    navController.navigate(it.first, it.second)
                } else {
                    navController.navigate(it.first)
                }
            }
        }
    }
}