package wook.pool.board.base

import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import org.greenrobot.eventbus.EventBus
import wook.pool.board.data.model.ThrowableEvent

abstract class BaseViewModel : ViewModel() {

    /**
     * CoroutineScope 내부 Exception 처리 Handler
     */
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleCoroutineException(throwable)
    }

    /**
     * Dispatchers 선언 (Normal Dispatchers + CoroutineExceptionHandler)
     */
    protected val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler

    protected val mainDispatchers = Dispatchers.Main + coroutineExceptionHandler

    private fun handleCoroutineException(throwable: Throwable) {
        Logger.e(">>> throwable : $throwable")
        EventBus.getDefault().post(ThrowableEvent(throwable))
    }
}