package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentScoreBoardBinding

class ScoreBoardFragment(override val layoutResId: Int = R.layout.fragment_score_board) :
    BaseFragment<FragmentScoreBoardBinding>(),
    View.OnClickListener,
    View.OnLongClickListener {

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = scoreBoardViewModel
                onClickListener = this@ScoreBoardFragment
                onLongClickListener = this@ScoreBoardFragment
                viewNineBall.setOnClickBall { isMoneyBall ->
                    scoreBoardViewModel.plusPoint(isMoneyBall)
                    if (isMoneyBall) {
                        scoreBoardViewModel.plusRunOut()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                textBtnScoreLeft -> {
                    scoreBoardViewModel.plusScore(true)
                }
                textBtnScoreRight -> {
                    scoreBoardViewModel.plusScore(false)
                }
                layoutBtnChangeTurn -> {
                    scoreBoardViewModel.switchTurn()
                    scoreBoardViewModel.switchRunOutMode(false)
                }
                layoutBtnFinishGame, layoutBtnCancelMatch -> {
                    scoreBoardViewModel.setScreenAction(R.id.action_fragment_score_board_to_fragment_choice_player)
                }
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            when (v) {
                textBtnScoreLeft -> {
                    scoreBoardViewModel.minusScore(true)
                }
                textBtnScoreRight -> {
                    scoreBoardViewModel.minusScore(false)
                }
            }
        }
        return true
    }
}