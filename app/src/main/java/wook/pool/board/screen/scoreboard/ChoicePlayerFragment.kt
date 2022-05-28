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
import wook.pool.board.base.Constant.BundleKey.IS_CHOICE_MODE_LEFT
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.databinding.FragmentChoicePlayerBinding

class ChoicePlayerFragment(override val layoutResId: Int = R.layout.fragment_choice_player) :
    BaseFragment<FragmentChoicePlayerBinding>(), View.OnClickListener {

    private val scoreBoardScreenViewModel: ScoreBoardScreenViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = playersViewModel
                listener = this@ChoicePlayerFragment
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                imgLeftCircle,
                imgBtnLeftChangePlayer,
                textBtnLeftChangePlayer -> {
                    val navDirection = ChoicePlayerFragmentDirections.actionFragmentChoicePlayerToFragmentPlayerList(true)
                    scoreBoardScreenViewModel.setNavDirection(navDirection)
                }
                imgRightCircle,
                imgBtnRightChangePlayer,
                textBtnRightChangePlayer -> {
                    val navDirection = ChoicePlayerFragmentDirections.actionFragmentChoicePlayerToFragmentPlayerList(false)
                    scoreBoardScreenViewModel.setNavDirection(navDirection)
                }
                layoutBtnClose -> {
                    activity?.finishAffinity()
                }
                imgBtnSelectGame -> {
                    Toast.makeText(hostActivityContext, "준비중입니다", Toast.LENGTH_SHORT).show()
                }
                layoutBtnChangeFirst -> {
                    playersViewModel.switchTurn()
                }
                layoutBtnStartGame -> {
                    val matchPlayers = playersViewModel.getMatchPlayers()
                    val navDirection = ChoicePlayerFragmentDirections.actionFragmentChoicePlayerToFragmentNineBall(matchPlayers)
                    scoreBoardScreenViewModel.setNavDirection(navDirection)
                }
                layoutBtnAdjustHandicap -> {
                    playersViewModel.switchHandicapAdjustment()
                }
                else -> {

                }
            }
        }
    }
}