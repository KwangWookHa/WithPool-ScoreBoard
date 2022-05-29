package wook.pool.board.base.event

import androidx.lifecycle.Observer

/**
 * Event Wrapper 를 위한 옵저버 클래스
 * getContentIfNotHandled() 체크를 옵저버 내에서 자동으로 수행
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit): Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}