package wook.pool.board.global.event

/**
 * Event Wrapper 를 위한 이벤트
 */
open class Event<out T>(private val content: T) {

    // 이벤트 처리 여부
    var hasBeenHandled = false
        private set

    /**
     * 이벤트가 처리되지 않았을 경우에 값을 반환
     * @return
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }


    /**
     * 이벤트 처리여부와 무관하게 값을 반환
     * @return
     */
    fun peekContent(): T = content
}