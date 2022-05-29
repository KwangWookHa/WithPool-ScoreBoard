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
        nineBallViewModel.isRegisterMatchSuccessful.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                showDialogToFinishMatch()
            }
        })
    }

    override fun onClick(v: View?) {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    layoutBtnFinishGame -> insertNineBallMatchResult()
                    layoutBtnCancelMatch -> showDialogToCancelMatch()
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
                    textBtnPlusLeftRunOut -> minusRunOut(true)
                    textBtnPlusRightRunOut -> minusRunOut(false)
                }
            }
        }
        return true
    }

    private fun showDialogToFinishMatch() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK)
                .setTitle(getString(R.string.fragment_nine_ball_finish_match))
                .setMessage(getString(R.string.fragment_nine_ball_register_successful))
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                    playersViewModel.initPlayers()
                    nineBallViewModel.initLiveData()
                    scoreBoardScreenViewModel.setNavDirection(
                        NineBallFragmentDirections.actionFragmentNineBallToFragmentChoicePlayer()
                    )
                }
                .create(context)
                .show()
        }
    }

    private fun showDialogToCancelMatch() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK_CANCEL)
                .setTitle(getString(R.string.fragment_nine_ball_cancel_match))
                .setMessage(getString(R.string.fragment_nine_ball_cancel_match_desc))
                .setLeftButtonText(getString(R.string.common_close))
                .setOnClickLeft { dialog ->
                    dialog.dismiss()
                }
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                    nineBallViewModel.initLiveData()
                    scoreBoardScreenViewModel.setNavDirection(
                        NineBallFragmentDirections.actionFragmentNineBallToFragmentChoicePlayer()
                    )
                }
                .create(context)
                .show()
        }
    }
}