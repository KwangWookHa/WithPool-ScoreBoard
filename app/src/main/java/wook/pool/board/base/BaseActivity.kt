package wook.pool.board.base

import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
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
}