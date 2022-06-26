package wook.pool.board.bindingadapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
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
}