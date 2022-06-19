package wook.pool.board.screen.playerlist

import androidx.recyclerview.widget.DiffUtil
import wook.pool.board.data.model.Player

class PlayerDiffUtil : DiffUtil.ItemCallback<Player>() {

    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.documentId == newItem.documentId

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

}