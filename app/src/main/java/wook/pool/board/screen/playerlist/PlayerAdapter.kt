package wook.pool.board.screen.playerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import wook.pool.board.base.BaseViewHolder
import wook.pool.board.data.model.Player
import wook.pool.board.databinding.ItemGridPlayerBinding

class PlayerAdapter(
    private val onClickPlayer: ((Player) -> Unit)
) : ListAdapter<Player, BaseViewHolder<Player>>(PlayerDiffUtil()) {

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