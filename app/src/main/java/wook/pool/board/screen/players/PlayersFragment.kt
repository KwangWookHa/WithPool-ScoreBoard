package wook.pool.board.screen.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import wook.pool.board.R
import wook.pool.board.data.enums.Handicap
import wook.pool.board.databinding.FragmentPlayersBinding
import wook.pool.board.global.base.BaseFragment
import wook.pool.board.global.event.EventObserver
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.scoreboard.ScoreBoardViewModel

class PlayersFragment(override val layoutResId: Int = R.layout.fragment_players) :
        BaseFragment<FragmentPlayersBinding>(),
        View.OnClickListener {

    private val args: PlayersFragmentArgs by navArgs()
    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()
    private val playerAdapter: PlayerAdapter by lazy {
        PlayerAdapter {
            playersViewModel.setPlayer(it, args.isModeChoiceLeft)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState).apply {
        binding.apply {
            selectedHandicapIndex = Handicap.HANDICAP_5.ordinal
            onClickHandicapButton = this@PlayersFragment.onClickHandicapButton
            listener = this@PlayersFragment
            recyclerPlayers.adapter = playerAdapter
            layoutPlayers.setOnRefreshListener { playersViewModel.getPlayers() }
        }
        initObserver()
    }

    private fun initObserver() {
        with(playersViewModel) {
            players.observe(viewLifecycleOwner) {
                selectHandicap(selectedHandicap.value ?: Handicap.HANDICAP_5)
            }
            selectedHandicap.observe(viewLifecycleOwner) {
                submitPlayers(it.value)
                binding.selectedHandicapIndex = it.ordinal
                binding.layoutPlayers.isRefreshing = false
            }
            isPlayerSetSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    playersViewModel.getHeadToHeadRecords()
                    scoreBoardViewModel.setNavDirection(PlayersFragmentDirections.actionFragmentPlayerListToFragmentSetting())
                } else {
                    showDialogDuplicatedPlayer()
                }
            })
        }
    }

    private fun submitPlayers(handicap: Int) {
        playersViewModel.players.value?.let {
            playerAdapter.submitList(it[handicap])
        }
    }

    private fun showDialogDuplicatedPlayer() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK)
                    .setTitle(getString(R.string.fragment_player_list_duplicated_player_title))
                    .setMessage(getString(R.string.fragment_player_list_duplicated_player_desc))
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                    }
                    .create(context)
                    .show()
        }
    }


    private val onClickHandicapButton = View.OnClickListener {
        with(binding) {
            val index = binding.layoutHandicapSelector.indexOfChild(it)
            if (selectedHandicapIndex == index) return@OnClickListener
            Handicap.values().firstOrNull { it.value == index + 3 }?.let { selectedHandicapIndex ->
                playersViewModel.selectHandicap(selectedHandicapIndex)
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                imgBtnBack ->
                    scoreBoardViewModel.setNavDirection(PlayersFragmentDirections.actionFragmentPlayerListToFragmentSetting())
                textBtnMinusHandicap -> {
                    playersViewModel.selectedHandicap.value?.ordinal?.minus(1)?.let {
                        kotlin.runCatching {
                            playersViewModel.selectHandicap(Handicap.values()[it])
                        }
                    }
                }
                textBtnPlusHandicap -> {
                    playersViewModel.selectedHandicap.value?.ordinal?.plus(1)?.let {
                        kotlin.runCatching {
                            playersViewModel.selectHandicap(Handicap.values()[it])
                        }
                    }
                }
                else -> return
            }
        }
    }
}