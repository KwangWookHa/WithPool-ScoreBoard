package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.base.Constant
import wook.pool.board.data.model.Player
import wook.pool.board.databinding.FragmentPlayerListBinding

class PlayerListFragment(override val layoutResId: Int = R.layout.fragment_player_list) :
    BaseFragment<FragmentPlayerListBinding>(),
    View.OnClickListener {

    companion object {
        const val POSITION_HANDICAP_3_4 = 0
        const val POSITION_HANDICAP_5_6 = 1
        const val POSITION_HANDICAP_7_8 = 2
        const val POSITION_HANDICAP_9_10 = 3
    }

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()

    private lateinit var leftAdapter: ChoicePlayerAdapter
    private lateinit var rightAdapter: ChoicePlayerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                selectedHandicapIndex = POSITION_HANDICAP_5_6
                viewModel = this@PlayerListFragment.scoreBoardViewModel
                listener = this@PlayerListFragment
            }
            arguments?.getBoolean(Constant.BundleKey.BUNDLE_KEY_ON_CHOICE_LEFT)?.let {
                scoreBoardViewModel.initMode(it)
            }
            initObserver()
            scoreBoardViewModel.getPlayers()
        }

    private fun initObserver() {
        with(scoreBoardViewModel) {
            playersByHandicap.observe(viewLifecycleOwner) {
                leftAdapter = getPlayerAdapter(it[5], true)
                binding.recyclerPlayersLeft.adapter = leftAdapter
                rightAdapter = getPlayerAdapter(it[6], false)
                binding.recyclerPlayersRight.adapter = rightAdapter
            }
        }
    }

    private fun getPlayerAdapter(players: List<Player>, isLeft: Boolean) =
        ChoicePlayerAdapter(players) { player ->
            scoreBoardViewModel.setPlayer(player, isLeft)
            activity?.onBackPressed()
        }

    private fun setPlayers(leftHandicap: Int, rightHandicap: Int) {
        scoreBoardViewModel.playersByHandicap.value?.let {
            leftAdapter.setPlayers(it[leftHandicap])
            rightAdapter.setPlayers(it[rightHandicap])
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                textBtnHandicap34 -> {
                    if (selectedHandicapIndex == POSITION_HANDICAP_3_4) return
                    selectedHandicapIndex = POSITION_HANDICAP_3_4
                    setPlayers(3, 4)
                }
                textBtnHandicap56 -> {
                    if (selectedHandicapIndex == POSITION_HANDICAP_5_6) return
                    selectedHandicapIndex = POSITION_HANDICAP_5_6
                    setPlayers(5, 6)
                }
                textBtnHandicap78 -> {
                    if (selectedHandicapIndex == POSITION_HANDICAP_7_8) return
                    selectedHandicapIndex = POSITION_HANDICAP_7_8
                    setPlayers(7, 8)
                }
                textBtnHandicap910 -> {
                    if (selectedHandicapIndex == POSITION_HANDICAP_9_10) return
                    selectedHandicapIndex = POSITION_HANDICAP_9_10
                    setPlayers(9, 10)
                }
                imgBtnBack -> {
                    activity?.onBackPressed()
                }
                else -> {

                }
            }
        }
    }
}