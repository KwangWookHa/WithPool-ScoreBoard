package wook.pool.board.bindingadapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.*
import androidx.databinding.BindingAdapter
import wook.pool.board.R
import wook.pool.board.base.Constant
import kotlin.math.absoluteValue

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

    @BindingAdapter("bindRemainingSeconds")
    @JvmStatic
    fun LinearLayoutCompat.bindRemainingSeconds(remainingSeconds: Int) {
        if (remainingSeconds == Constant.IS_NOT_INITIALIZED) return
        if (remainingSeconds == 40) {
            forEach { it.visibility = View.VISIBLE }
            return
        }
        val index = (remainingSeconds - 39).absoluteValue
        this[index].visibility = View.INVISIBLE
    }
}