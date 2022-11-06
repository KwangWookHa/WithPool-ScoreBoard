package wook.pool.board.screen.players

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import wook.pool.board.data.model.Player
import wook.pool.board.databinding.ItemGridPlayerBinding
import wook.pool.board.global.base.BaseViewHolder

class PlayerAdapter(
        private val onClickPlayer: ((Player) -> Unit)
) : ListAdapter<Player, BaseViewHolder<Player>>(object : DiffUtil.ItemCallback<Player>() {

    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem.documentId == newItem.documentId

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem == newItem

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Player> {
        val binding = ItemGridPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Player>, position: Int) {
        holder.bind(position, getItem(position), onClickPlayer)
    }

    inner class GridPlayerViewHolder(private val binding: ItemGridPlayerBinding) : BaseViewHolder<Player>(binding) {
        override fun bind(position: Int, item: Player, listener: ((Player) -> Unit)?) {
            binding.player = item
            binding.onClickPlayer = listener
        }
    }

}