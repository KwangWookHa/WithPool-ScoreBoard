package wook.pool.board.screen.players

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import wook.pool.board.data.model.Player
import wook.pool.board.databinding.ItemGridAddPlayerBinding
import wook.pool.board.databinding.ItemGridPlayerBinding
import wook.pool.board.global.base.BaseViewHolder

// RecyclerView에 표시할 아이템 타입 정의
sealed class PlayerListItem {
    data class PlayerItem(val player: Player) : PlayerListItem()
    object AddPlayerItem : PlayerListItem()
}

class PlayerAdapter(
        private val onClickPlayer: ((Player) -> Unit),
        private val onLongClickPlayer: ((Player) -> Unit)? = null,
        private val onClickAddPlayer: (() -> Unit)? = null
) : ListAdapter<PlayerListItem, BaseViewHolder<*>>(object : DiffUtil.ItemCallback<PlayerListItem>() {

    override fun areItemsTheSame(oldItem: PlayerListItem, newItem: PlayerListItem): Boolean {
        return when {
            oldItem is PlayerListItem.PlayerItem && newItem is PlayerListItem.PlayerItem ->
                oldItem.player.documentId == newItem.player.documentId
            oldItem is PlayerListItem.AddPlayerItem && newItem is PlayerListItem.AddPlayerItem ->
                true
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: PlayerListItem, newItem: PlayerListItem): Boolean =
            oldItem == newItem

}) {

    companion object {
        private const val VIEW_TYPE_PLAYER = 0
        private const val VIEW_TYPE_ADD_PLAYER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PlayerListItem.PlayerItem -> VIEW_TYPE_PLAYER
            is PlayerListItem.AddPlayerItem -> VIEW_TYPE_ADD_PLAYER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_PLAYER -> {
                val binding = ItemGridPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GridPlayerViewHolder(binding)
            }
            VIEW_TYPE_ADD_PLAYER -> {
                val binding = ItemGridAddPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AddPlayerViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (val item = getItem(position)) {
            is PlayerListItem.PlayerItem -> {
                (holder as GridPlayerViewHolder).bind(position, item.player, onClickPlayer)
            }
            is PlayerListItem.AddPlayerItem -> {
                (holder as AddPlayerViewHolder).bind(position, Unit, null)
            }
        }
    }

    inner class GridPlayerViewHolder(private val binding: ItemGridPlayerBinding) : BaseViewHolder<Player>(binding) {
        override fun bind(position: Int, item: Player, listener: ((Player) -> Unit)?) {
            binding.player = item
            binding.onClickPlayer = listener
            
            // 롱클릭 이벤트 설정
            binding.root.setOnLongClickListener {
                onLongClickPlayer?.invoke(item)
                true
            }
        }
    }

    inner class AddPlayerViewHolder(private val binding: ItemGridAddPlayerBinding) : BaseViewHolder<Unit>(binding) {
        override fun bind(position: Int, item: Unit, listener: ((Unit) -> Unit)?) {
            binding.onClickAddPlayer = onClickAddPlayer
        }
    }
}
