package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentPlayerListBinding

class PlayerListFragment(override val layoutResId: Int= R.layout.fragment_player_list) : BaseFragment<FragmentPlayerListBinding>() {

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        super.onCreateView(inflater, container, savedInstanceState).apply {
            binding?.apply {

            }
        }


}