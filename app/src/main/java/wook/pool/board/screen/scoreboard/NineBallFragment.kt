package wook.pool.board.screen.scoreboard

import android.os.Bundle
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
import wook.pool.board.screen.playerlist.PlayersViewModel

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
            setLoadingProgress(true)
            binding.apply {
                viewModel = nineBallViewModel.apply {
                    initMatch(args.matchPlayers)
                }
                onClickListener = this@NineBallFragment
                onLongClickListener = this@NineBallFragment
            }
            initObserver()
        }
    }

    private fun initObserver() {
        with(nineBallViewModel) {
            documentPath.observe(viewLifecycleOwner) {
                if (it.isNotBlank()) {
                    setLoadingProgress(false)
                }
            }
            isSetMatchSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    setLoadingProgress(false)
                    showDialogToFinishMatch()
                }
            })
            isDeleteMatchSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    setLoadingProgress(false)
                    backToChoicePlayerFragment()
                }
            })
            isMatchOver.observe(viewLifecycleOwner) {
                if (it) {
                    showDialogToRegisterMatch()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    layoutBtnFinishGame -> {
                        setLoadingProgress(true)
                        finishNineBallMatch()
                    }
                    layoutBtnCancelMatch -> showDialogToCancelMatch()
                    layoutLeftPlayer, textBtnScoreLeft -> setScore(true, +1)
                    layoutRightPlayer, textBtnScoreRight -> setScore(false, +1)
                    textBtnPlusLeftRunOut -> setRunOut(true, +1)
                    textBtnPlusRightRunOut -> setRunOut(false, +1)
                    else -> {}
                }
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    textBtnScoreLeft -> setScore(true, -1)
                    textBtnScoreRight -> setScore(false, -1)
                    textBtnPlusLeftRunOut -> setRunOut(true, -1)
                    textBtnPlusRightRunOut -> setRunOut(false, -1)
                }
            }
        }
        return true
    }

    private fun showDialogToRegisterMatch() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK_CANCEL)
                .setImgResourceId(R.drawable.ic_upload_48)
                .setIsIconVisible(true)
                .setMessage(getString(R.string.fragment_nine_ball_would_you_register_match))
                .setLeftButtonText(getString(R.string.common_move_to_back))
                .setOnClickLeft { it.dismiss() }
                .setRightButtonText(getString(R.string.common_register))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                    nineBallViewModel.finishNineBallMatch()
                }
                .create(context)
                .show()
        }
    }

    private fun showDialogToFinishMatch() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                .setType(DefaultDialog.DialogType.DIALOG_OK)
                .setImgResourceId(R.drawable.ic_success_64)
                .setIsIconVisible(true)
                .setMessage(getString(R.string.fragment_nine_ball_register_successful))
                .setBackPressDisabled(true)
                .setRightButtonText(getString(R.string.common_confirm))
                .setOnClickRight { dialog ->
                    dialog.dismiss()
                    backToChoicePlayerFragment()
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
                    setLoadingProgress(true)
                    nineBallViewModel.deleteNineBallMatch()
                }
                .create(context)
                .show()
        }
    }

    private fun backToChoicePlayerFragment() {
        playersViewModel.initPlayers()
        nineBallViewModel.initLiveData()
        scoreBoardScreenViewModel.setNavDirection(
            NineBallFragmentDirections.actionFragmentNineBallToFragmentSetting()
        )
    }

    private fun setLoadingProgress(flag: Boolean) {
        scoreBoardScreenViewModel.setLoadingProgress(flag)
    }
}