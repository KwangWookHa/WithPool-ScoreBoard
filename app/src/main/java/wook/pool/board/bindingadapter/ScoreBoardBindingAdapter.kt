package wook.pool.board.bindingadapter

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import wook.pool.board.R

object ScoreBoardBindingAdapter {

    @BindingAdapter("bindAdjustedHandicap")
    @JvmStatic
    fun AppCompatTextView.bindAdjustedHandicap(handicap: Int) {
        text = resources.getString(
            when (handicap) {
                -1 -> {
                    R.string.fragment_choice_player_adjust_handicap_1
                }
                -2 -> {
                    R.string.fragment_choice_player_adjust_handicap_2
                }
                else -> {
                    R.string.fragment_choice_player_adjust_handicap_0
                }
            }
        )
    }

    @BindingAdapter(value = ["bindHandicap", "bindAdjustedHandicap"], requireAll = true)
    @JvmStatic
    fun AppCompatTextView.bindAdjustedHandicapInChoice(handicap: Int, adjustedHandicap: Int) {
        this.text = if (handicap == adjustedHandicap) {
            handicap.toString()
        } else {
            resources.getString(
                R.string.fragment_choice_player_handicap,
                handicap,
                adjustedHandicap
            )
        }
    }
}