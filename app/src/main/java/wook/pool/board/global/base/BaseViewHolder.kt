package wook.pool.board.global.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(_binding: ViewDataBinding) : RecyclerView.ViewHolder(_binding.root) {

    abstract fun bind(position: Int, item: T, listener: ((T) -> Unit)?)

}