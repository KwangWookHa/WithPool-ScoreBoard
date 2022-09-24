package wook.pool.board.screen.scoreboard.anycall

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wook.pool.board.R
import wook.pool.board.global.base.BaseFragment
import wook.pool.board.Constant
import wook.pool.board.databinding.FragmentAnycallBinding
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.dialog.DiceDialog
import wook.pool.board.screen.dialog.SetTotalDialog
import wook.pool.board.screen.scoreboard.ScoreBoardViewModel

class AnyCallFragment(override val layoutResId: Int = R.layout.fragment_anycall) :
        BaseFragment<FragmentAnycallBinding>(),
        View.OnClickListener, View.OnLongClickListener {

    private var soundPool: SoundPool? = null
    private var soundScore: Int = Constant.IS_NOT_INITIALIZED

    private val diceDialog: DiceDialog by lazy {
        DiceDialog(hostActivityContext!!)
    }

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
    private val anyCallViewModel: AnyCallViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            initSoundPool()
            binding.apply {
                viewModel = anyCallViewModel
                onClickListener = this@AnyCallFragment
                onLongClickListener = this@AnyCallFragment
            }
            initObserver()
        }
    }

    private fun initObserver() {
        with(anyCallViewModel) {
            isMatchOver.observe(viewLifecycleOwner) {
                if (it) {
                    showDialogToFinishMatch()
                }
            }
            playerLeftDice.observe(viewLifecycleOwner) {
                if (diceDialog.isShowing) {
                    diceDialog.dismiss()
                }
            }
        }
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build()
        soundScore = soundPool?.load(activity, R.raw.score, 1) ?: return
    }

    private fun playSound(sound: Int) {
        if (sound == Constant.IS_NOT_INITIALIZED) return
        soundPool?.play(sound, 1f, 1f, 0, 0, 1f)
    }

    override fun onStop() {
        super.onStop()
        soundPool?.release()
        soundPool = null
    }

    override fun onClick(v: View?) {
        with(binding) {
            with(anyCallViewModel) {
                when (v) {
                    layoutBtnFinishGame -> {
                        showDialogToFinishMatch()
                    }
                    layoutLeftPlayer, textBtnScoreLeft -> {
                        playSound(soundScore)
                        setScore(true, +1)
                    }
                    layoutRightPlayer, textBtnScoreRight -> {
                        playSound(soundScore)
                        setScore(false, +1)
                    }
                    textBtnTotalSetCountLeft, textBtnTotalSetCountRight -> {
                        showSetTotalDialog(v == textBtnTotalSetCountLeft)
                    }
                    imgBtnDice -> {
                        diceDialog.show()
                        anyCallViewModel.randomizeDice()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showSetTotalDialog(isLeft: Boolean) {
        hostActivityContext?.let {
            SetTotalDialog.Builder()
                    .setOnClickCancel { dialog -> dialog.cancel() }
                    .setOnClickConfirm { dialog, input ->
                        anyCallViewModel.setTotal(isLeft, if (input.isBlank()) 30 else input.toInt())
                        dialog.dismiss()
                    }
                    .create(it)
                    .show()
        }
    }

    override fun onLongClick(v: View?): Boolean {
        with(binding) {
            with(anyCallViewModel) {
                when (v) {
                    textBtnScoreLeft -> {
                        if (playerLeftScore.value!! > 0) {
                            setScore(true, -1)
                        } else {
                            showSetTotalDialog(true)
                        }
                    }
                    textBtnScoreRight -> {
                        if (playerRightScore.value!! > 0) {
                            setScore(false, -1)
                        } else {
                            showSetTotalDialog(false)
                        }
                    }
                }
            }
        }
        return true
    }


    private fun showDialogToFinishMatch() {
        hostActivityContext?.let { context ->
            DefaultDialog.Builder()
                    .setType(DefaultDialog.DialogType.DIALOG_OK_CANCEL)
                    .setTitle(getString(R.string.fragment_nine_ball_finish_match))
                    .setMessage(getString(R.string.fragment_anycall_cancel_match_desc))
                    .setLeftButtonText(getString(R.string.common_close))
                    .setOnClickLeft { dialog ->
                        dialog.dismiss()
                    }
                    .setRightButtonText(getString(R.string.common_confirm))
                    .setOnClickRight { dialog ->
                        dialog.dismiss()
                        backToSettingFragment()
                    }
                    .create(context)
                    .show()
        }
    }

    private fun backToSettingFragment() {
        anyCallViewModel.initLiveData()
        scoreBoardViewModel.setNavDirection(
                AnyCallFragmentDirections.actionFragmentAnycallToFragmentSetting()
        )
    }
}