package wook.pool.board.bindingadapter

import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.databinding.BindingAdapter
import wook.pool.board.R

object PlayerListBindingAdapter {

    @BindingAdapter("bindSelectedHandicap")
    @JvmStatic
    fun LinearLayoutCompat.bindSelectedHandicap(selectedHandicapIndex: Int) {
        forEachIndexed { index, view ->
            val textView = view as AppCompatTextView
            textView.setBackgroundResource(
                    if (selectedHandicapIndex == index) {
                        R.drawable.bg_main_round_6
                    } else {
                        R.drawable.bg_gray_round_6
                    }
            )
            textView.setTextColor(
                    ContextCompat.getColor(
                            context,
                            if (selectedHandicapIndex == index) {
                                android.R.color.white
                            } else {
                                android.R.color.black
                            }
                    )
            )
        }
    }
}