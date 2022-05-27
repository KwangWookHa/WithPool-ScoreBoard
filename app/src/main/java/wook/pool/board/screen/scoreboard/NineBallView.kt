package wook.pool.board.screen.scoreboard

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import wook.pool.board.databinding.LayoutNineBallViewBinding

class NineBallView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs),
    View.OnClickListener, View.OnLongClickListener {

    private val binding: LayoutNineBallViewBinding =
        LayoutNineBallViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            listener = this@NineBallView
        }

    private val layouts =
        with(binding) {
            listOf(
                layoutBall1,
                layoutBall2,
                layoutBall3,
                layoutBall4,
                layoutBall5,
                layoutBall6,
                layoutBall7,
                layoutBall8,
                layoutBall9
            )
        }


    private var onClickBall: ((Boolean) -> Unit)? = null

    fun setOnClickBall(onClickBall: (Boolean) -> Unit) {
        this.onClickBall = onClickBall
    }

    private fun setAllVisibility(visibility: Int) {
        layouts.forEach {
            it.visibility = visibility
        }
    }

    private fun setVisibility(position: Int, isVisible: Boolean) {
        layouts[position].visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                layoutBall1 -> onClickBall(0)
                layoutBall2 -> onClickBall(1)
                layoutBall3 -> onClickBall(2)
                layoutBall4 -> onClickBall(3)
                layoutBall5 -> onClickBall(4)
                layoutBall6 -> onClickBall(5)
                layoutBall7 -> onClickBall(6)
                layoutBall8 -> onClickBall(7)
                layoutBall9 -> {
                    onClickBall(8, true)
                    Handler(Looper.getMainLooper()).postDelayed({
                        setAllVisibility(View.VISIBLE)
                    }, 500)
                }
                else -> {}
            }
        }
    }

    private fun onClickBall(position: Int, isMoneyBall: Boolean = false) {
        onClickBall?.invoke(isMoneyBall)
        setVisibility(position, false)
    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            when (v) {
                layoutBall1 -> {

                }
                layoutBall2 -> {

                }
                layoutBall3 -> {

                }
                layoutBall4 -> {

                }
                layoutBall5 -> {

                }
                layoutBall6 -> {

                }
                layoutBall7 -> {

                }
                layoutBall8 -> {

                }
                layoutBall9 -> {

                }
            }
        }
        return true
    }
}