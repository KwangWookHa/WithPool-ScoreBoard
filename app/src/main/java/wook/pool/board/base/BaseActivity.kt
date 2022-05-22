package wook.pool.board.base

import android.content.Intent
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wook.pool.board.R
import wook.pool.board.data.ThrowableEvent
import wook.pool.board.screen.dialog.DefaultDialog

open class BaseActivity : AppCompatActivity() {

    var backPressCount = 0

    fun startActivity(
        destination: Class<*>,
        finish: Boolean = false,
        putExtra: ((Intent) -> Unit)? = null
    ) {
        val intent = Intent(this, destination).also {
            putExtra?.invoke(it)
        }
        startActivity(intent)
        if (finish) finish()
    }

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
        handleError(event.throwable)
    }

    private fun handleError(throwable: Throwable) {
        when (throwable.cause) {

        }
    }


    /**
     * 정의한 에러 처리
     */
    private fun handleWalletError(message: String?) {
        Logger.e("ERROR\n$message")
//        val description = getString(
//                when (message) {
//
//                }
//        )
//        showErrorDialog(description)
    }

    private fun showErrorDialog(message: String) {
        DefaultDialog.Builder()
            .setType(DefaultDialog.DialogType.DIALOG_OK)
            .setTitle("")
            .setMessage(message)
            .setGravity(Gravity.CENTER)
            .setRightButtonText(getString(R.string.common_confirm))
            .setOnClickRight { it.dismiss() }
            .create(this)
            .show()
    }
}