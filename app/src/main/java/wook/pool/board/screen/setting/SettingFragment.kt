package wook.pool.board.screen.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.global.base.BaseFragment
import wook.pool.board.databinding.FragmentSettingBinding
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.dialog.DiceDialog
import wook.pool.board.screen.players.PlayersViewModel
import wook.pool.board.screen.scoreboard.ScoreBoardViewModel

class SettingFragment(override val layoutResId: Int = R.layout.fragment_setting) :
        BaseFragment<FragmentSettingBinding>(), View.OnClickListener {

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()

    private val diceDialog: DiceDialog by lazy {
        DiceDialog(hostActivityContext!!)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = playersViewModel
                listener = this@SettingFragment
            }
            initObserver()
        }
    }

    private fun initObserver() {
        playersViewModel.playerLeftDice.observe(viewLifecycleOwner) {
            if (diceDialog.isShowing) {
                diceDialog.dismiss()
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                imgLeftCircle,
                textBtnLeftChangePlayer,
                textBtnLeftChangePlayer -> {
                    val navDirection = SettingFragmentDirections.actionFragmentSettingToFragmentPlayerList(true)
                    scoreBoardViewModel.setNavDirection(navDirection)
                }
                imgRightCircle,
                textBtnRightChangePlayer,
                textBtnRightChangePlayer -> {
                    val navDirection = SettingFragmentDirections.actionFragmentSettingToFragmentPlayerList(false)
                    scoreBoardViewModel.setNavDirection(navDirection)
                }
                layoutBtnClose -> {
                    activity?.finishAffinity()
                }
                imgBtnSelectGame -> {
                    Toast.makeText(hostActivityContext, getString(R.string.common_coming_soon), Toast.LENGTH_SHORT).show()
                }
                layoutBtnStartGame -> {
                    val matchPlayers = playersViewModel.getMatchPlayers() ?: let {
                        showDialogPlayerNotSet()
                        return
                    }
                    val navDirection = SettingFragmentDirections.actionFragmentSettingToFragmentNineBall(
                            matchPlayers,
                            playersViewModel.isTimerMode.value ?: false
                    )
                    scoreBoardViewModel.setNavDirection(navDirection)
                }
                textBtnAdjustHandicap -> {
                    playersViewModel.switchHandicapAdjustment()
                }
                imgBtnDice -> {
                    playersViewModel.getMatchPlayers() ?: let {
                        showDialogPlayerNotSet()
                        return
                    }
                    diceDialog.show()
                    playersViewModel.randomizeDice()
                }
                textBtnTimerMode -> {
                    playersViewModel.switchTimer()
                }
                textBtnAnyCallGame -> {
                    scoreBoardViewModel.setNavDirection(SettingFragmentDirections.actionFragmentSettingToFragmentAnycall())
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