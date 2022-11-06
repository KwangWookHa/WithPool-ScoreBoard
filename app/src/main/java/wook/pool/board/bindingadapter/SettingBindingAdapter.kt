package wook.pool.board.bindingadapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import wook.pool.board.R

object SettingBindingAdapter {

    @BindingAdapter("bindDiceNumber")
    @JvmStatic
    fun AppCompatImageView.bindDiceNumber(number: Int) {
        if (number == 0) {
            visibility = View.INVISIBLE
            return
        }
        setImageResource(
                when (number) {
                    1 -> R.drawable.ic_dice_1
                    2 -> R.drawable.ic_dice_2
                    3 -> R.drawable.ic_dice_3
                    4 -> R.drawable.ic_dice_4
                    5 -> R.drawable.ic_dice_5
                    else -> R.drawable.ic_dice_6
                }
        )
        visibility = View.VISIBLE
    }

    @BindingAdapter(value = ["bindOpponentPlayerName", "bindHeadToHeadRecords"], requireAll = true)
    @JvmStatic
    fun AppCompatTextView.bindHeadToHeadRecords(opponentPlayerName: String?, records: Triple<Int, Int, Int>?) {
        if (opponentPlayerName == null || records == null || records.toList().all { it == 0 }) {
            this.visibility = View.GONE
            return
        }
        this.text = context.getString(
                R.string.fragment_choice_player_head_to_head_records,
                opponentPlayerName,
                records.first,
                records.second,
                records.third
        )
        this.visibility = View.VISIBLE
    }
}