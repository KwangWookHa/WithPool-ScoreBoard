package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.base.Constant.BundleKey.BUNDLE_KEY_ON_CHOICE_LEFT
import wook.pool.board.databinding.FragmentChoicePlayerBinding

class ChoicePlayerFragment(override val layoutResId: Int = R.layout.fragment_choice_player) :
    BaseFragment<FragmentChoicePlayerBinding>(), View.OnClickListener {

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = scoreBoardViewModel
                listener = this@ChoicePlayerFragment
            }
            initObserver()
        }
    }

    private fun initObserver() {
        with(scoreBoardViewModel) {
            playerLeftHandicap.observe(viewLifecycleOwner) {}
            playerRightHandicap.observe(viewLifecycleOwner) {}
            isGameOver.observe(viewLifecycleOwner) {}
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                imgLeftCircle,
                imgBtnLeftChangePlayer,
                textBtnLeftChangePlayer -> {
                    scoreBoardViewModel.setScreenAction(
                        R.id.action_fragment_choice_player_to_fragment_player_list,
                        bundleOf(BUNDLE_KEY_ON_CHOICE_LEFT to true)
                    )
                }
                imgRightCircle,
                imgBtnRightChangePlayer,
                textBtnRightChangePlayer -> {
                    scoreBoardViewModel.setScreenAction(
                        R.id.action_fragment_choice_player_to_fragment_player_list,
                        bundleOf(BUNDLE_KEY_ON_CHOICE_LEFT to false)
                    )
                }
                layoutBtnClose -> {
                    activity?.finishAffinity()
                }
                imgBtnSelectGame -> {
                    Toast.makeText(hostActivityContext, "준비중입니다", Toast.LENGTH_SHORT).show()
                }
                layoutBtnChangeFirst -> {
                    scoreBoardViewModel.switchTurn()
                }
                layoutBtnStartGame -> {
                    scoreBoardViewModel.setScreenAction(R.id.action_fragment_choice_player_to_fragment_score_board)
                    scoreBoardViewModel.plusTurnCount()
                }
                layoutBtnAdjustHandicap -> {
                    scoreBoardViewModel.switchHandicapAdjustment()
                }
                else -> {

                }
            }
        }
    }
}