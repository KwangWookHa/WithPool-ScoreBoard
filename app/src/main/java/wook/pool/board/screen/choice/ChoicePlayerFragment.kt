package wook.pool.board.screen.choice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentChoicePlayerBinding
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.playerlist.PlayersViewModel
import wook.pool.board.screen.scoreboard.ScoreBoardScreenViewModel

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
                textBtnLeftChangePlayer,
                textBtnLeftChangePlayer -> {
                    val navDirection = ChoicePlayerFragmentDirections.actionFragmentChoicePlayerToFragmentPlayerList(true)
                    scoreBoardScreenViewModel.setNavDirection(navDirection)
                }
                imgRightCircle,
                textBtnRightChangePlayer,
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
                layoutBtnStartGame -> {
                    val matchPlayers = playersViewModel.getMatchPlayers() ?: null.also {
                        showDialogPlayerNotSet()
                        return
                    }
                    matchPlayers?.let {
                        val navDirection = ChoicePlayerFragmentDirections.actionFragmentChoicePlayerToFragmentNineBall(it)
                        scoreBoardScreenViewModel.setNavDirection(navDirection)
                    }
                }
                textBtnAdjustHandicap -> {
                    playersViewModel.switchHandicapAdjustment()
                }
                else -> {

                }
            }
        }
    }

    private fun showDialogPlayerNotSet() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK)
                .setTitle(getString(R.string.fragment_choice_player_not_set))
                .setMessage(getString(R.string.fragment_choice_player_not_set_desc))
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                }
                .create(context)
                .show()
        }
    }
}