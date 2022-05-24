package wook.pool.board.base

import android.view.View

interface OnItemClickListener<T> {
    /**
     * 아이템을 클릭했을 때 호출.
     */
    fun onItemClick(view: View, position: Int, data: T)
}
