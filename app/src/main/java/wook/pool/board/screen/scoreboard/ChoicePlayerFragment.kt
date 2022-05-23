package wook.pool.board.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.base.BaseFragment
import wook.pool.board.databinding.FragmentChoicePlayerBinding

class ChoicePlayerFragment(override val layoutResId: Int = R.layout.fragment_choice_player) :
    BaseFragment<FragmentChoicePlayerBinding>(), View.OnClickListener {

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding?.apply {
                viewModel = scoreBoardViewModel
                listener = this@ChoicePlayerFragment
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding!!) {
            when(v) {
                imgLeftCircle,
                imgBtnLeftChangePlayer,
                textBtnLeftChangePlayer -> {

                }
                imgRightCircle,
                imgBtnRightChangePlayer,
                textBtnRightChangePlayer-> {

                }
                layoutBtnClose -> {
                    activity?.finish()
                }
                imgBtnSelectGame -> {
                    Toast.makeText(hostActivityContext, "준비중입니다", Toast.LENGTH_SHORT).show()
                }
                layoutBtnChangeFirst -> {
                    scoreBoardViewModel.switchKickOff()
                }
                layoutBtnStartGame -> {

                }
                else -> {

                }
            }
        }
    }
}