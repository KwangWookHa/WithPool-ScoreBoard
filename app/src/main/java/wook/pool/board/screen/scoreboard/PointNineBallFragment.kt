package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentPointNineBallBinding

class PointNineBallFragment(override val layoutResId: Int = R.layout.fragment_point_nine_ball) :
    BaseFragment<FragmentPointNineBallBinding>(),
    View.OnClickListener,
    View.OnLongClickListener {

    private val scoreBoardScreenViewModel: ScoreBoardScreenViewModel by activityViewModels()
    private val pointNineBallViewModel: PointNineBallViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = pointNineBallViewModel
                onClickListener = this@PointNineBallFragment
                onLongClickListener = this@PointNineBallFragment
                viewNineBall.setOnClickBall { isMoneyBall ->
                    pointNineBallViewModel.plusPoint(isMoneyBall)
                    if (isMoneyBall) {
                        pointNineBallViewModel.plusRunOut()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                textBtnScoreLeft -> {
                    pointNineBallViewModel.plusScore(true)
                }
                textBtnScoreRight -> {
                    pointNineBallViewModel.plusScore(false)
                }
                layoutBtnChangeTurn -> {
                    pointNineBallViewModel.switchTurn()
                    pointNineBallViewModel.switchRunOutMode(false)
                }
                layoutBtnFinishGame, layoutBtnCancelMatch -> {

                }
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            when (v) {
                textBtnScoreLeft -> {
                    pointNineBallViewModel.minusScore(true)
                }
                textBtnScoreRight -> {
                    pointNineBallViewModel.minusScore(false)
                }
            }
        }
        return true
    }
}