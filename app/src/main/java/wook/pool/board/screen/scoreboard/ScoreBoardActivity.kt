package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wook.pool.board.R
import wook.pool.board.base.BaseActivity
import wook.pool.board.databinding.ActivityScoreBoardBinding
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.dialog.ProgressDialog
import wook.pool.board.screen.setting.InitViewModel


@AndroidEntryPoint
class ScoreBoardActivity : BaseActivity() {

    private var binding: ActivityScoreBoardBinding? = null
    private val progressDialog by lazy { ProgressDialog(this) }

    private val scoreBoardScreenViewModel: ScoreBoardScreenViewModel by viewModels()
    private val initViewModel: InitViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerScoreBoard) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_score_board)
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
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun initObserver() {
        with(scoreBoardScreenViewModel) {
            setLoadingProgress(true)
            navDirection.observe(this@ScoreBoardActivity) {
                navController.navigate(it)
            }
            isLoading.observe(this@ScoreBoardActivity) {
                if (it) {
                    progressDialog.show()
                } else {
                    progressDialog.hide()
                }
            }
        }
        with(initViewModel) {
            isUpdateForced.observe(this@ScoreBoardActivity) {
                scoreBoardScreenViewModel.setLoadingProgress(false)
                if (it) {
                    DefaultDialog.Builder()
                        .setType(DefaultDialog.DialogType.DIALOG_OK)
                        .setTitle(getString(R.string.app_version_update))
                        .setMessage(getString(R.string.app_version_download_up_to_date_version))
                        .setRightButtonText(getString(R.string.common_confirm))
                        .setOnClickRight { dialog ->
                            dialog.dismiss()
                            finishAffinity()
                        }
                        .setBackPressDisabled(true)
                        .create(this@ScoreBoardActivity)
                        .show()
                } else {
                    signInAnonymously()
                }
            }
        }
    }

    override fun onBackPressed() {
        when (++backPressCount) {
            1 -> Toast.makeText(this, getString(R.string.common_on_back_pressed_notice), Toast.LENGTH_SHORT).show()
            2 -> finishAffinity()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            kotlin.runCatching {
                backPressCount = 0
            }
        }
    }
}