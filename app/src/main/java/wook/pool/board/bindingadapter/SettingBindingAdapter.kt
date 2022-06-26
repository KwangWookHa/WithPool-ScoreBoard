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
            visibility = View.GONE
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

    @BindingAdapter("bindTimerMode")
    @JvmStatic
    fun AppCompatTextView.bindTimerMode(mode: Boolean) {
        if (mode) {
            text = context.getString(R.string.fragment_choice_player_timer_mode_on)
            alpha = 1f
            setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            text = context.getString(R.string.fragment_choice_player_timer_mode_off)
            alpha = 0.3f
            setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
        }
    }
}