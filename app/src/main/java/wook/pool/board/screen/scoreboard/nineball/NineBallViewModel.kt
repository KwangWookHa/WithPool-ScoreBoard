package wook.pool.board.screen.scoreboard.nineball

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.global.base.BaseViewModel
import wook.pool.board.Constant
import wook.pool.board.global.event.Event
import wook.pool.board.global.extension.plus
import wook.pool.board.data.enums.GameType
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.match.AddNineBallMatchUseCase
import wook.pool.board.domain.usecase.match.DeleteNineBallMatchUseCase
import wook.pool.board.domain.usecase.match.UpdateNineBallMatchUseCase
import javax.inject.Inject

@HiltViewModel
class NineBallViewModel @Inject constructor(
        private val addNineBallMatchUseCase: AddNineBallMatchUseCase,
        private val updateNineBallMatchUseCase: UpdateNineBallMatchUseCase,
        private val deleteNineBallMatchUseCase: DeleteNineBallMatchUseCase,
) : BaseViewModel() {

    /***************************** Player Left *****************************/

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    private val _playerLeftAdjustedHandicap: MutableLiveData<Int> = MutableLiveData(Constant.IS_NOT_INITIALIZED)
    val playerLeftAdjustedHandicap: LiveData<Int> = _playerLeftAdjustedHandicap

    private val _playerLeftScore: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftScore: LiveData<Int> = _playerLeftScore

    private val _playerLeftRunOut: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftRunOut: LiveData<Int> = _playerLeftRunOut

    private val _playerLeftAlpha: MutableLiveData<Float> = MutableLiveData(1f)
    val playerLeftAlpha: LiveData<Float> = _playerLeftAlpha

    /***************************** Player Right *****************************/

    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    private val _playerRightAdjustedHandicap: MutableLiveData<Int> = MutableLiveData()
    val playerRightAdjustedHandicap: LiveData<Int> = _playerRightAdjustedHandicap

    private val _playerRightScore: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightScore: LiveData<Int> = _playerRightScore

    private val _playerRightRunOut: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightRunOut: LiveData<Int> = _playerRightRunOut

    private val _playerRightAlpha: MutableLiveData<Float> = MutableLiveData(1f)
    val playerRightAlpha: LiveData<Float> = _playerRightAlpha

    /***************************** Player Right *****************************/

    val isGuestMode: Boolean get() = _playerLeft.value?.name == "Guest" || _playerRight.value?.name == "Guest"

    val isMatchOver: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.value = false
        addSource(_playerLeftScore) {
            if (_playerLeftAdjustedHandicap.value == null) return@addSource
            this.value = if (_playerLeftAdjustedHandicap.value!! == it) {
                _isPlayerLeftWinner.postValue(true)
                _playerRightAlpha.postValue(0.4f)
                true
            } else {
                _playerRightAlpha.postValue(1f)
                false
            }
        }
        addSource(_playerRightScore) {
            if (_playerRightAdjustedHandicap.value == null) return@addSource
            this.value = if (_playerRightAdjustedHandicap.value!! == it) {
                _isPlayerLeftWinner.postValue(false)
                _playerLeftAlpha.postValue(0.4f)
                true
            } else {
                _playerLeftAlpha.postValue(1f)
                false
            }
        }
    }

    private val _isPlayerLeftWinner: MutableLiveData<Boolean> = MutableLiveData()
    val isPlayerLeftWinner: LiveData<Boolean> = _isPlayerLeftWinner

    private val _documentPath: MutableLiveData<String> = MutableLiveData()
    val documentPath: LiveData<String> = _documentPath

    private val _isFinishMatchSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isFinishMatchSuccessful: LiveData<Event<Boolean>> = _isFinishMatchSuccessful

    private val _isDeleteMatchSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isDeleteMatchSuccessful: LiveData<Event<Boolean>> = _isDeleteMatchSuccessful

    private val _isTimerMode: MutableLiveData<Boolean> = MutableLiveData()
    val isTimerMode: LiveData<Boolean> = _isTimerMode

    private val _remainingSeconds: MutableLiveData<Int> = MutableLiveData(Constant.IS_NOT_INITIALIZED)
    val remainingSeconds: LiveData<Int> = _remainingSeconds

    private var _timer: CountDownTimer? = object : CountDownTimer(40_000L, 1_000L) {
        override fun onTick(millisUntilFinished: Long) {
            val seconds = if (millisUntilFinished % 1000 == 0L) {
                (millisUntilFinished / 1000L).toInt()
            } else {
                ((((40_000L - millisUntilFinished) % 1000) + millisUntilFinished) / 1000L).toInt()
            }
            _remainingSeconds.value = seconds
        }

        override fun onFinish() {
            _remainingSeconds.value = 0
            cancel()
        }
    }

    fun initMatch(matchPlayers: MatchPlayers?) {
        viewModelScope.launch(ioDispatchers) {
            matchPlayers?.let {
                _playerLeft.postValue(it.playerLeft)
                _playerRight.postValue(it.playerRight)
                _playerLeftAdjustedHandicap.postValue(it.playerLeft.handicap?.plus(it.adjustment))
                _playerRightAdjustedHandicap.postValue(it.playerRight.handicap?.plus(it.adjustment))
                initNineBallMatch(it.playerLeft, it.playerRight, it.adjustment)
            }
        }
    }

    fun initTimer(mode: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            _isTimerMode.postValue(mode)
        }
    }

    fun setScore(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                if (variation < 0 && _playerLeftScore.value!! == 0) return@launch
                if (_playerLeftScore.value!! + variation > _playerLeftAdjustedHandicap.value!!) return@launch
            } else {
                if (variation < 0 && _playerRightScore.value!! == 0) return@launch
                if (_playerRightScore.value!! + variation > _playerRightAdjustedHandicap.value!!) return@launch
            }

            _documentPath.value?.let {
                val setScore = {
                    if (isLeft) {
                        _playerLeftScore.plus(variation)
                    } else {
                        _playerRightScore.plus(variation)
                    }
                }
                if (it.isNotBlank() && !isGuestMode) {
                    kotlin.runCatching {
                        updateNineBallMatchUseCase.invoke(
                                documentPath = it,
                                data = mapOf(
                                        if (isLeft) {
                                            Constant.Field.FILED_PLAYER_LEFT_SCORE to _playerLeftScore.value!! + variation
                                        } else {
                                            Constant.Field.FILED_PLAYER_RIGHT_SCORE to _playerRightScore.value!! + variation
                                        }
                                ))
                    }.onSuccess {
                        setScore()
                    }
                } else {
                    setScore()
                }
            }
        }
    }

    fun setRunOut(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                if (_playerLeftScore.value!! + variation > _playerLeftAdjustedHandicap.value!!
                        || _playerLeftScore.value!! + variation < 0
                        || _playerLeftRunOut.value!! + variation < 0
                ) return@launch
            } else {
                if (_playerRightScore.value!! + variation > _playerRightAdjustedHandicap.value!!
                        || _playerRightScore.value!! + variation < 0
                        || _playerRightScore.value!! + variation < 0
                ) return@launch
            }

            _documentPath.value?.let {
                val setRunOut = {
                    if (isLeft) {
                        _playerLeftRunOut.plus(variation)
                        _playerLeftScore.plus(variation)
                    } else {
                        _playerRightRunOut.plus(variation)
                        _playerRightScore.plus(variation)
                    }
                }
                if (it.isNotBlank() && !isGuestMode) {
                    kotlin.runCatching {
                        updateNineBallMatchUseCase.invoke(
                                documentPath = it,
                                data = hashMapOf<String, Int>().apply {
                                    if (isLeft) {
                                        put(Constant.Field.FILED_PLAYER_LEFT_SCORE, _playerLeftScore.value!! + variation)
                                        put(Constant.Field.FILED_PLAYER_LEFT_RUN_OUT, _playerLeftRunOut.value!! + variation)
                                    } else {
                                        put(Constant.Field.FILED_PLAYER_RIGHT_RUN_OUT, _playerRightRunOut.value!! + variation)
                                        put(Constant.Field.FILED_PLAYER_RIGHT_SCORE, _playerRightScore.value!! + variation)
                                    }
                                })
                    }.onSuccess {
                        setRunOut()
                    }
                } else {
                    setRunOut()
                }
            }
        }
    }

    private fun initNineBallMatch(playerLeft: Player, playerRight: Player, adjustment: Int) {
        viewModelScope.launch(ioDispatchers) {
            if (playerLeft.name == "Guest" || playerRight.name == "Guest") return@launch
            kotlin.runCatching {
                addNineBallMatchUseCase.invoke(
                        NineBallMatch(
                                gameType = GameType.GAME_9_BALL.text,
                                adjustment = adjustment,
                                isLive = true,
                                players = listOf(playerLeft.name, playerRight.name),
                                playerLeftName = playerLeft.name,
                                playerRightName = playerRight.name,
                                playerLeftRunOut = null,
                                playerRightRunOut = null,
                                playerLeftScore = null,
                                playerRightScore = null,
                                playerWinnerName = null,
                                playerLoserName = null,
                                matchStartTimeStamp = Timestamp.now(),
                                matchEndTimeStamp = null,
                        )
                )
            }.onSuccess {
                _documentPath.postValue(it.id)
            }
        }
    }

    fun finishNineBallMatch() {
        viewModelScope.launch(ioDispatchers) {
            _documentPath.value?.let {
                if (it.isNotBlank() && isMatchOver.value!! && !isGuestMode) {
                    val winnerName = (if (_isPlayerLeftWinner.value!!) _playerLeft.value?.name else _playerRight.value?.name) ?: ""
                    val loserName = (if (_isPlayerLeftWinner.value!!) _playerRight.value?.name else _playerLeft.value?.name) ?: ""
                    kotlin.runCatching {
                        updateNineBallMatchUseCase.invoke(
                                documentPath = it,
                                data = hashMapOf<String, Any>().apply {
                                    put(Constant.Field.FILED_IS_LIVE, false)
                                    put(Constant.Field.FILED_PLAYER_WINNER_NAME, winnerName)
                                    put(Constant.Field.FILED_PLAYER_LOSER_NAME, loserName)
                                    put(Constant.Field.FILED_END_TIME_STAMP, Timestamp.now())
                                })
                    }.onSuccess {
                        _documentPath.postValue("")
                        _isFinishMatchSuccessful.postValue(Event(true))
                    }
                }
            }
        }
    }

    fun deleteNineBallMatch() {
        viewModelScope.launch(ioDispatchers) {
            _documentPath.value?.let {
                if (it.isNotBlank() && !isGuestMode) {
                    kotlin.runCatching {
                        deleteNineBallMatchUseCase.invoke(it)
                    }.onSuccess {
                        _isDeleteMatchSuccessful.postValue(Event(true))
                    }
                } else {
                    _isDeleteMatchSuccessful.postValue(Event(true))
                }
            }
        }
    }

    fun rewindTimer() {
        _timer?.cancel()
        _timer?.start()
    }


    fun initLiveData() {
        viewModelScope.launch(ioDispatchers) {
            _playerLeft.postValue(null)
            _playerLeftAdjustedHandicap.postValue(Constant.IS_NOT_INITIALIZED)
            _playerLeftScore.postValue(0)
            _playerLeftRunOut.postValue(0)

            _playerRight.postValue(null)
            _playerRightAdjustedHandicap.postValue(Constant.IS_NOT_INITIALIZED)
            _playerRightScore.postValue(0)
            _playerRightRunOut.postValue(0)

            isMatchOver.postValue(false)
            _playerLeftAlpha.postValue(1f)
            _playerRightAlpha.postValue(1f)

            _documentPath.postValue("")
            _isFinishMatchSuccessful.postValue(Event(false))
        }
    }
}