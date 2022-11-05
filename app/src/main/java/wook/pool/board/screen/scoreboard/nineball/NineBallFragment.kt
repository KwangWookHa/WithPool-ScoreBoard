package wook.pool.board.screen.scoreboard.nineball

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import wook.pool.board.Constant
import wook.pool.board.R
import wook.pool.board.databinding.FragmentNineBallBinding
import wook.pool.board.global.base.BaseFragment
import wook.pool.board.global.event.EventObserver
import wook.pool.board.screen.dialog.DefaultDialog
import wook.pool.board.screen.players.PlayersViewModel
import wook.pool.board.screen.scoreboard.ScoreBoardViewModel
import kotlin.random.Random

class NineBallFragment(override val layoutResId: Int = R.layout.fragment_nine_ball) :
        BaseFragment<FragmentNineBallBinding>(),
        View.OnClickListener,
        View.OnLongClickListener {

    private var soundPool: SoundPool? = null
    private var soundScore: Int = Constant.IS_NOT_INITIALIZED
    private var soundRunOut: Int = Constant.IS_NOT_INITIALIZED
    private var soundOldRunOut: Int = Constant.IS_NOT_INITIALIZED
    private var soundTimerBeep: Int = Constant.IS_NOT_INITIALIZED

    private val scoreBoardViewModel: ScoreBoardViewModel by activityViewModels()
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
            initSoundPool()
            binding.apply {
                viewModel = nineBallViewModel.apply {
                    initMatch(args.matchPlayers)
                    initTimer(args.isTimerMode)
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
                setLoadingProgress(false)
            }
            isFinishMatchSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    setLoadingProgress(false)
                    hostActivityContext?.let { context ->
                        Toast.makeText(context, getString(R.string.fragment_nine_ball_register_successful), Toast.LENGTH_SHORT).show()
                    }
                    backToSettingFragment()
                }
            })
            isDeleteMatchSuccessful.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    setLoadingProgress(false)
                    backToSettingFragment()
                }
            })
            isMatchOver.observe(viewLifecycleOwner) {
                if (it && !isGuestModeValue) {
                    showDialogToRegisterMatch()
                }
            }
            remainingSeconds.observe(viewLifecycleOwner) {
                if (it <= 4) {
                    playSound(soundTimerBeep)
                }
            }
            isGuestMode.observe(viewLifecycleOwner) {
                if (it) setLoadingProgress(false)
            }
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            with(nineBallViewModel) {
                when (v) {
                    layoutBtnFinishGame -> {
                        if (isGuestModeValue) {
                            backToSettingFragment()
                            return
                        }
                        setLoadingProgress(true)
                        finishNineBallMatch()
                    }
                    layoutBtnCancelMatch -> showDialogToCancelMatch()
                    layoutLeftPlayer, textBtnScoreLeft, layoutRightPlayer, textBtnScoreRight -> {
                        val isLeft = v == layoutLeftPlayer || v == textBtnScoreLeft
                        playSound(soundScore)
                        setScore(isLeft, +1)
                    }
                    textBtnPlusLeftRunOut, textBtnPlusRightRunOut -> {
                        val isLeft = v == textBtnPlusLeftRunOut
                        playSound(if (Random.nextBoolean()) soundRunOut else soundOldRunOut)
                        setRunOut(isLeft, +1)
                    }
                    imgRewindTimer -> {
                        rewindTimer()
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

    private fun backToSettingFragment() {
        playersViewModel.initDice()
        playersViewModel.initPlayers()
        nineBallViewModel.initLiveData()
        scoreBoardViewModel.setNavDirection(
                NineBallFragmentDirections.actionFragmentNineBallToFragmentSetting()
        )
    }

    private fun setLoadingProgress(flag: Boolean) {
        scoreBoardViewModel.setLoadingProgress(flag)
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build()
        soundScore = soundPool?.load(activity, R.raw.score, 1) ?: return
        soundRunOut = soundPool?.load(activity, R.raw.runout, 1) ?: return
        soundOldRunOut = soundPool?.load(activity, R.raw.old_runout, 1) ?: return
        soundTimerBeep = soundPool?.load(activity, R.raw.timer_beep, 1) ?: return
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
}