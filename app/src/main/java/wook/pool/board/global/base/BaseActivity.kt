package wook.pool.board.global.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wook.pool.board.R
import wook.pool.board.data.model.ThrowableEvent
import wook.pool.board.screen.dialog.DefaultDialog

open class BaseActivity : AppCompatActivity() {

    var backPressCount = 0

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }


    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 공통 에러 처리
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onThrowableEvent(event: ThrowableEvent) {
        val message = when (event.throwable.message) {
            else -> {
                ""
            }
        }
        showErrorDialog(message)
    }

    private fun showErrorDialog(message: String) {
        DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK)
                .setTitle("")
                .setMessage(message)
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { it.dismiss() }
                .create(this)
                .show()
    }

    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
                window,
                window.decorView
        ).show(WindowInsetsCompat.Type.systemBars())
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