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
import wook.pool.board.data.enums.AppVersionStatus
import wook.pool.board.global.base.BaseActivity
import wook.pool.board.databinding.ActivityScoreBoardBinding
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.dialog.ProgressDialog


@AndroidEntryPoint
class ScoreBoardActivity : BaseActivity() {

    private var binding: ActivityScoreBoardBinding? = null
    private val progressDialog by lazy { ProgressDialog(this) }

    private val scoreBoardViewModel: ScoreBoardViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerScoreBoard) as NavHostFragment
    }
    private val navController: NavController by lazy { navHostFragment.navController }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityScoreBoardBinding?>(this, R.layout.activity_score_board).apply {
            lifecycleOwner = this@ScoreBoardActivity
        }
        initObserver()
        observeAppVersion()
        scoreBoardViewModel.setLoadingProgress(true)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun initObserver() {
        with(scoreBoardViewModel) {
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
    }

    private fun observeAppVersion() {
        with(scoreBoardViewModel) {
            appVersionStatus.observe(this@ScoreBoardActivity) {
                when (it) {
                    AppVersionStatus.UPDATE_IMMEDIATELY -> showDialogToUpdateImmediately()
                    AppVersionStatus.UPDATE_AVAILABLE -> showDialogToUpdateAvailable()
                    AppVersionStatus.UP_TO_DATE -> scoreBoardViewModel.setLoadingProgress(false)
                }
                scoreBoardViewModel.setLoadingProgress(false)
            }
        }
    }

    private fun showDialogToUpdateImmediately() {
        DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK)
                .setTitle(getString(R.string.app_version_update))
                .setMessage(getString(R.string.app_version_update_forced))
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                    finishAffinity()
                }
                .setBackPressDisabled(true)
                .create(this@ScoreBoardActivity)
                .show()
    }

    private fun showDialogToUpdateAvailable() {
        DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK)
                .setTitle(getString(R.string.app_version_update))
                .setMessage(getString(R.string.app_version_update_available))
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                }
                .setBackPressDisabled(true)
                .create(this@ScoreBoardActivity)
                .show()
    }
}