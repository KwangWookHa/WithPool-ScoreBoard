package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.base.Constant
import wook.pool.board.databinding.FragmentPlayerListBinding

class PlayerListFragment(override val layoutResId: Int = R.layout.fragment_player_list) :
    BaseFragment<FragmentPlayerListBinding>(),
    View.OnClickListener {

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()

    private lateinit var leftAdapter: ChoicePlayerAdapter
    private lateinit var rightAdapter: ChoicePlayerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                this?.selectedHandicapIndex = 1
                this?.viewModel = this@PlayerListFragment.scoreBoardViewModel
                this?.listener = this@PlayerListFragment
            }
            arguments?.let { scoreBoardViewModel.initArguments(it) }
            initObserver()
            scoreBoardViewModel.getPlayers()
        }

    private fun initObserver() {
        with(scoreBoardViewModel) {
            playersHandicap5.observe(viewLifecycleOwner) {
                leftAdapter = ChoicePlayerAdapter(it) {}
                binding?.recyclerPlayersLeft?.adapter = leftAdapter
            }
            playersHandicap6.observe(viewLifecycleOwner) {
                rightAdapter = ChoicePlayerAdapter(it) {}
                binding?.recyclerPlayersRight?.adapter = rightAdapter
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                this?.textBtnHandicap34 -> binding?.selectedHandicapIndex = 0
                this?.textBtnHandicap56 -> binding?.selectedHandicapIndex = 1
                this?.textBtnHandicap78 -> binding?.selectedHandicapIndex = 2
                this?.textBtnHandicap910 -> binding?.selectedHandicapIndex = 3
            }
        }
    }
}