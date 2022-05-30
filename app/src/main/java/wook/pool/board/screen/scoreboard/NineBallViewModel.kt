package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.event.Event
import wook.pool.board.base.minus
import wook.pool.board.base.plus
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.NineBallMatchResult
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.InsertNineBallMatchResultUseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NineBallViewModel @Inject constructor(
    private val insertNineBallMatchResultUseCase: InsertNineBallMatchResultUseCase,
) : BaseViewModel() {

    private var startTimeStamp: Long = 0L
        set(value) {
            dateTime = Date(value)
            field = value
        }
    private lateinit var dateTime: Date

    /***************************** Player Left *****************************/

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    private val _playerLeftAdjustedHandicap: MutableLiveData<Int> = MutableLiveData()
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

    private val _gameType: MutableLiveData<GameType> = MutableLiveData(GameType.GAME_9_BALL)
    val gameType: LiveData<GameType> = _gameType

    private val _isRegisterMatchSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isRegisterMatchSuccessful: LiveData<Event<Boolean>> = _isRegisterMatchSuccessful

    fun initMatch(matchPlayers: MatchPlayers?) {
        startTimeStamp = System.currentTimeMillis()
        matchPlayers?.let {
            _playerLeft.value = it.playerLeft
            _playerRight.value = it.playerRight
            _playerLeftAdjustedHandicap.value = it.playerLeftAdjustedHandicap
            _playerRightAdjustedHandicap.value = it.playerRightAdjustedHandicap
        }
    }

    fun plusScore(isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isMatchOver.value != true) {
                if (isLeft) {
                    (_playerLeftScore.value!! + 1).let {
                        if (it > _playerLeftAdjustedHandicap.value!!) return@launch
                        _playerLeftScore.plus(1)
                    }
                } else {
                    (_playerRightScore.value!! + 1).let {
                        if (it > _playerRightAdjustedHandicap.value!!) return@launch
                        _playerRightScore.plus(1)
                    }
                }
            }
        }
    }

    fun minusScore(isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                _playerLeftScore.minus(1)
            } else {
                _playerRightScore.minus(1)
            }
        }
    }

    fun plusRunOut(isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isMatchOver.value != true) {
                if (isLeft) {
                    _playerLeftRunOut.plus(1)
                } else {
                    _playerRightRunOut.plus(1)
                }
            }
        }
    }

    fun minusRunOut(isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                _playerLeftRunOut.minus(1)
            } else {
                _playerRightRunOut.minus(1)
            }
        }
    }

    fun insertNineBallMatchResult() {
        viewModelScope.launch(ioDispatchers) {
            val playerLeft = _playerLeft.value
            val playerRight = _playerRight.value
            if (playerLeft == null || playerRight == null) return@launch

            val endTimeMillis = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy.MM.dd_HH:mm", Locale.KOREA)

            insertNineBallMatchResultUseCase(
                nineBallMatchResult = NineBallMatchResult(
                    gameType = GameType.GAME_9_BALL.text,
                    adjustment = _playerLeftAdjustedHandicap.value!! - playerLeft.handicap!!,
                    isLive = false,
                    playerLeftName = playerLeft.name,
                    playerRightName = playerRight.name,
                    playerLeftRunOut = _playerLeftRunOut.value ?: 0,
                    playerRightRunOut = _playerRightRunOut.value ?: 0,
                    playerLeftScore = _playerLeftScore.value,
                    playerRightScore = _playerRightScore.value,
                    playerWinnerName = if (_isPlayerLeftWinner.value!!) playerLeft.name else playerRight.name,
                    playerLoserName = if (_isPlayerLeftWinner.value!!) playerRight.name else playerLeft.name,
                    matchStartTimeStamp = startTimeStamp,
                    matchEndTimeStamp = endTimeMillis,
                    matchStartDateTime = sdf.format(dateTime),
                    matchEndDateTime = sdf.format(Date(endTimeMillis))
                ),
                onSuccess = {
                    _isRegisterMatchSuccessful.postValue(Event(true))
                },
                onFailure = {
                    throw it
                }
            )
        }
    }

    fun initLiveData() {
        viewModelScope.launch(ioDispatchers) {
            _playerLeft.postValue(null)
            _playerLeftAdjustedHandicap.postValue(0)
            _playerLeftScore.postValue(0)
            _playerLeftRunOut.postValue(0)

            _playerRight.postValue(null)
            _playerRightAdjustedHandicap.postValue(0)
            _playerRightScore.postValue(0)
            _playerRightRunOut.postValue(0)

            isMatchOver.postValue(false)
            _playerLeftAlpha.postValue(1f)
            _playerRightAlpha.postValue(1f)
        }
    }
}