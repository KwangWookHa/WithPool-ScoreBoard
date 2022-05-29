package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentNineBallBinding
import wook.pool.board.databinding.FragmentPointNineBallBinding

class NineBallFragment(override val layoutResId: Int = R.layout.fragment_nine_ball) :
    BaseFragment<FragmentNineBallBinding>(),
    View.OnClickListener,
    View.OnLongClickListener {

    private val scoreBoardScreenViewModel: ScoreBoardScreenViewModel by activityViewModels()
    private val nineBallViewModel: NineBallViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()

    private val args: NineBallFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = nineBallViewModel.apply { initMatchPlayers(args.matchPlayers) }
                onClickListener = this@NineBallFragment
                onLongClickListener = this@NineBallFragment
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    layoutBtnFinishGame, layoutBtnCancelMatch -> {
                        playersViewModel.initPlayers()
                        scoreBoardScreenViewModel.setScreenActionId(R.id.action_fragment_nine_ball_to_fragment_choice_player)
                    }
                    layoutLeftPlayer, textBtnScoreLeft -> plusScore(true)
                    layoutRightPlayer, textBtnScoreRight -> plusScore(false)
                    textBtnPlusLeftRunOut -> {
                        plusRunOut(true)
                        plusScore(true)
                    }
                    textBtnPlusRightRunOut -> {
                        plusRunOut(false)
                        plusScore(false)
                    }
                    else -> {}
                }
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    textBtnScoreLeft -> minusScore(true)
                    textBtnScoreRight -> minusScore(false)
                }
            }
        }
        return true
    }
}