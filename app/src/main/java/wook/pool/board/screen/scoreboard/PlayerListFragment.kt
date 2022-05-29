package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.base.event.EventObserver
import wook.pool.board.data.model.Player
import wook.pool.board.data.model.PlayersSelectedIndex
import wook.pool.board.databinding.FragmentPlayerListBinding
import wook.pool.board.screen.dialog.DefaultDialog

class PlayerListFragment(override val layoutResId: Int = R.layout.fragment_player_list) :
    BaseFragment<FragmentPlayerListBinding>(),
    View.OnClickListener {

    private val scoreBoardScreenViewModel: ScoreBoardScreenViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()

    private lateinit var leftAdapter: ChoicePlayerAdapter
    private lateinit var rightAdapter: ChoicePlayerAdapter

    private val args: PlayerListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                selectedHandicapIndex = PlayersSelectedIndex.INDEX_HANDICAP_5_6.index
                listener = this@PlayerListFragment
            }
            initObserver()
        }

    private fun initObserver() {
        with(playersViewModel) {
            playersByHandicap.observe(viewLifecycleOwner) {
                if (binding.recyclerPlayersLeft.adapter == null && binding.recyclerPlayersRight.adapter == null) {
                    leftAdapter = getPlayerAdapter(it[5], args.isModeChoiceLeft)
                    binding.recyclerPlayersLeft.adapter = leftAdapter
                    rightAdapter = getPlayerAdapter(it[6], args.isModeChoiceLeft)
                    binding.recyclerPlayersRight.adapter = rightAdapter
                }
            }
            selectedHandicapIndex.observe(viewLifecycleOwner) {
                binding.selectedHandicapIndex = it.index
                setPlayers(it.handicapLeft, it.handicapRight)
            }
            isPlayerSetSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    scoreBoardScreenViewModel.setNavDirection(
                        PlayerListFragmentDirections.actionFragmentPlayerListToFragmentChoicePlayer()
                    )
                } else {
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
            })
        }
    }

    private fun getPlayerAdapter(players: List<Player>, isLeft: Boolean) =
        ChoicePlayerAdapter(players) { player ->
            playersViewModel.setPlayer(player, isLeft)
        }

    private fun setPlayers(leftHandicap: Int, rightHandicap: Int) {
        playersViewModel.playersByHandicap.value?.let {
            leftAdapter.setPlayers(it[leftHandicap])
            rightAdapter.setPlayers(it[rightHandicap])
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                textBtnHandicap34 -> {
                    if (selectedHandicapIndex == PlayersSelectedIndex.INDEX_HANDICAP_3_4.index) return
                    playersViewModel.setSelectedHandicapIndex(PlayersSelectedIndex.INDEX_HANDICAP_3_4)
                    setPlayers(3, 4)
                }
                textBtnHandicap56 -> {
                    if (selectedHandicapIndex == PlayersSelectedIndex.INDEX_HANDICAP_5_6.index) return
                    playersViewModel.setSelectedHandicapIndex(PlayersSelectedIndex.INDEX_HANDICAP_5_6)
                    setPlayers(5, 6)
                }
                textBtnHandicap78 -> {
                    if (selectedHandicapIndex == PlayersSelectedIndex.INDEX_HANDICAP_7_8.index) return
                    playersViewModel.setSelectedHandicapIndex(PlayersSelectedIndex.INDEX_HANDICAP_7_8)
                    setPlayers(7, 8)
                }
                textBtnHandicap910 -> {
                    if (selectedHandicapIndex == PlayersSelectedIndex.INDEX_HANDICAP_9_10.index) return
                    playersViewModel.setSelectedHandicapIndex(PlayersSelectedIndex.INDEX_HANDICAP_9_10)
                    setPlayers(9, 10)
                }
                imgBtnBack -> {
                    scoreBoardScreenViewModel.setNavDirection(
                        PlayerListFragmentDirections.actionFragmentPlayerListToFragmentChoicePlayer()
                    )
                }
                else -> {

                }
            }
        }
    }
}