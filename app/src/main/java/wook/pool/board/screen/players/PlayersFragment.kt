package wook.pool.board.screen.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import wook.pool.board.R
import wook.pool.board.global.base.BaseFragment
import wook.pool.board.global.event.EventObserver
import wook.pool.board.data.model.SelectedHandicapIndex
import wook.pool.board.databinding.FragmentPlayerListBinding
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.scoreboard.ScoreBoardViewModel

class PlayersFragment(override val layoutResId: Int = R.layout.fragment_player_list) :
        BaseFragment<FragmentPlayerListBinding>(),
        View.OnClickListener {

    companion object {
        private const val INITIAL_SELECTED_HANDICAP = 5
    }

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
    ): View? =
            super.onCreateView(inflater, container, savedInstanceState).apply {
                binding.apply {
                    selectedHandicapIndex = SelectedHandicapIndex.INDEX_HANDICAP_5.index
                    onClickHandicap = this@PlayersFragment.onClickHandicap
                    listener = this@PlayersFragment
                    recyclerPlayers.adapter = playerAdapter
                }
                initObserver()
            }

    private fun initObserver() {
        with(playersViewModel) {
            playersByHandicap.observe(viewLifecycleOwner) {
                if (selectedHandicapIndex.value == null) {
                    submitPlayers(INITIAL_SELECTED_HANDICAP)
                }
            }
            selectedHandicapIndex.observe(viewLifecycleOwner) {
                binding.selectedHandicapIndex = it.index
                submitPlayers(it.handicap)
            }
            isPlayerSetSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    scoreBoardViewModel.setNavDirection(
                            PlayersFragmentDirections.actionFragmentPlayerListToFragmentSetting()
                    )
                } else {
                    showDialogDuplicatedPlayer()
                }
            })
        }
    }

    private fun submitPlayers(handicap: Int) {
        playersViewModel.playersByHandicap.value?.let {
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


    private val onClickHandicap = View.OnClickListener {
        with(binding) {
            val index = binding.layoutHandicapSelector.indexOfChild(it)
            if (selectedHandicapIndex == index) return@OnClickListener
            SelectedHandicapIndex.values().firstOrNull { it.handicap == index + 3 }?.let { selectedHandicapIndex ->
                playersViewModel.setSelectedHandicapIndex(selectedHandicapIndex)
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                imgBtnBack -> {
                    scoreBoardViewModel.setNavDirection(
                            PlayersFragmentDirections.actionFragmentPlayerListToFragmentSetting()
                    )
                }
            }
        }
    }
}