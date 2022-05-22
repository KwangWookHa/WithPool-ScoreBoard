package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentChoicePlayerBinding

class ChoicePlayerFragment(override val layoutResId: Int = R.layout.fragment_choice_player) : BaseFragment<FragmentChoicePlayerBinding>() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {

            }
        }
    }

}