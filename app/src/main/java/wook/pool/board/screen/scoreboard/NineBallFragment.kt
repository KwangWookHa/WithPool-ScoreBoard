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
import wook.pool.board.databinding.FragmentNineBallBinding
import wook.pool.board.screen.dialog.DefaultDialog

class NineBallFragment(override val layoutResId: Int = R.layout.fragment_nine_ball) :
    BaseFragment<FragmentNineBallBinding>(),
    View.OnClickListener,
    View.OnLongClickListener {

    private val scoreBoardScreenViewModel: ScoreBoardScreenViewModel by activityViewModels()
    private val nineBallViewModel: NineBallViewModel by activityViewModels()
    private val playersViewModel: PlayersViewModel by activityViewModels()

    private val args: NineBallFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            binding.apply {
                viewModel = nineBallViewModel.apply { initMatch(args.matchPlayers) }
                onClickListener = this@NineBallFragment
                onLongClickListener = this@NineBallFragment
            }
            initObserver()
        }
    }

    private fun initObserver() {
        nineBallViewModel.isInsertSuccess.observe(viewLifecycleOwner) {
            if (it) {
                hostActivityContext?.let { context ->
                    DefaultDialog.Builder()
                        .setType(DefaultDialog.DialogType.DIALOG_OK)
                        .setTitle(getString(R.string.fragment_nine_ball_match_finished))
                        .setMessage(getString(R.string.fragment_nine_ball_upload_successful))
                        .setGravity(Gravity.CENTER)
                        .setRightButtonText(getString(R.string.common_confirm))
                        .setOnClickRight { dialog ->
                            dialog.dismiss()
                            playersViewModel.initPlayers()
                            scoreBoardScreenViewModel.setScreenActionId(R.id.action_fragment_nine_ball_to_fragment_choice_player)
                        }
                        .create(context)
                        .show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    layoutBtnFinishGame, layoutBtnCancelMatch -> insertNineBallMatchResult()
                    layoutLeftPlayer, textBtnScoreLeft -> plusScore(true)
                    layoutRightPlayer, textBtnScoreRight -> plusScore(false)
                    textBtnPlusLeftRunOut -> {
                        plusRunOut(true)
                        plusScore(true)
                    }
                    textBtnPlusRightRunOut -> {
                        plusRunOut(false)
                        plusScore(false)
                    }
                    else -> {}
                }
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    textBtnScoreLeft -> minusScore(true)
                    textBtnScoreRight -> minusScore(false)
                }
            }
        }
        return true
    }
}