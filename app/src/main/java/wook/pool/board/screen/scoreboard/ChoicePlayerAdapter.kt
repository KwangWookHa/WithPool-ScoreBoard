package wook.pool.board.screen.scoreboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wook.pool.board.base.BaseViewHolder
import wook.pool.board.data.model.Player
import wook.pool.board.databinding.ItemChoicePlayerBinding

class ChoicePlayerAdapter(
    private val players: List<Player>,
    private val onClickPlayer: ((Player) -> Unit),
) : RecyclerView.Adapter<ChoicePlayerAdapter.ChoicePlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoicePlayerViewHolder {
        val binding = ItemChoicePlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChoicePlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChoicePlayerViewHolder, position: Int) {
        holder.bind(position, players[position], onClickPlayer)
    }

    override fun getItemCount(): Int = players.size

    inner class ChoicePlayerViewHolder(private val binding: ItemChoicePlayerBinding) : BaseViewHolder<Player>(binding) {

        override fun bind(position: Int, item: Player, listener: ((Player) -> Unit)?) {
            binding.player = item
            binding.onClickPlayer = listener
        }
    }
}