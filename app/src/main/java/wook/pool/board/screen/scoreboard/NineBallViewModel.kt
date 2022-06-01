package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.event.Event
import wook.pool.board.base.minus
import wook.pool.board.base.plus
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.AddNineBallMatchUseCase
import wook.pool.board.domain.usecase.DeleteNineBallMatchUseCase
import wook.pool.board.domain.usecase.SetNineBallMatchUseCase
import javax.inject.Inject

@HiltViewModel
class NineBallViewModel @Inject constructor(
    private val addNineBallMatchUseCase: AddNineBallMatchUseCase,
    private val setNineBallMatchUseCase: SetNineBallMatchUseCase,
    private val deleteNineBallMatchUseCase: DeleteNineBallMatchUseCase,
) : BaseViewModel() {

    private lateinit var startTimeStamp: Timestamp

    /***************************** Player Left *****************************/

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    private val _playerLeftAdjustedHandicap: MutableLiveData<Int> = MutableLiveData(-9999)
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

    private val _documentReferenceId: MutableLiveData<String> = MutableLiveData()
    val documentReferenceId: LiveData<String> = _documentReferenceId

    private val _isSetMatchSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isSetMatchSuccessful: LiveData<Event<Boolean>> = _isSetMatchSuccessful

    private val _isDeleteMatchSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isDeleteMatchSuccessful: LiveData<Event<Boolean>> = _isDeleteMatchSuccessful

    fun initMatch(matchPlayers: MatchPlayers?) {
        viewModelScope.launch(ioDispatchers) {
            matchPlayers?.let {
                startTimeStamp = Timestamp.now()
                _playerLeft.postValue(it.playerLeft)
                _playerRight.postValue(it.playerRight)
                _playerLeftAdjustedHandicap.postValue(it.playerLeft.handicap?.plus(it.adjustment))
                _playerRightAdjustedHandicap.postValue(it.playerRight.handicap?.plus(it.adjustment))
                addNineBallMatch(it.playerLeft, it.playerRight, it.adjustment)
            }
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

    private fun addNineBallMatch(playerLeft: Player, playerRight: Player, adjustment: Int) {
        viewModelScope.launch(ioDispatchers) {
            addNineBallMatchUseCase(
                nineBallMatch = NineBallMatch(
                    gameType = GameType.GAME_9_BALL.text,
                    adjustment = adjustment,
                    isLive = true,
                    playerLeftName = playerLeft.name,
                    playerRightName = playerRight.name,
                    playerLeftRunOut = null,
                    playerRightRunOut = null,
                    playerLeftScore = null,
                    playerRightScore = null,
                    playerWinnerName = null,
                    playerLoserName = null,
                    matchStartDateTime = startTimeStamp,
                    matchEndDateTime = null,
                ),
                onSuccess = { _documentReferenceId.postValue(it.id) },
                onFailure = { throw it }
            )
        }
    }

    fun setNineBallMatch() {
        viewModelScope.launch(ioDispatchers) {
            _documentReferenceId.value?.let {
                if (it.isNotBlank()) {
                    setNineBallMatchUseCase(
                        documentReferenceId = it,
                        nineBallMatch = NineBallMatch(
                            isLive = false,
                            playerLeftRunOut = _playerLeftRunOut.value ?: 0,
                            playerRightRunOut = _playerRightRunOut.value ?: 0,
                            playerLeftScore = _playerLeftScore.value,
                            playerRightScore = _playerRightScore.value,
                            playerWinnerName = if (_isPlayerLeftWinner.value!!) _playerLeft.value?.name else _playerRight.value?.name,
                            playerLoserName = if (_isPlayerLeftWinner.value!!) _playerRight.value?.name else _playerLeft.value?.name,
                            matchEndDateTime = Timestamp.now(),
                        ),
                        mergeFields = listOf(
                            "isLive",
                            "playerLeftRunOut",
                            "playerRightRunOut",
                            "playerLeftScore",
                            "playerRightScore",
                            "playerWinnerName",
                            "playerLoserName",
                            "matchEndDateTime"
                        ),
                        onSuccess = { _isSetMatchSuccessful.postValue(Event(true)) },
                        onFailure = { throw it }
                    )
                }
            }
        }
    }

    fun deleteNineBallMatch() {
        viewModelScope.launch(ioDispatchers) {
            _documentReferenceId.value?.let {
                if (it.isNotBlank()) {
                    deleteNineBallMatchUseCase(
                        documentReferenceId = it,
                        onSuccess = { _isDeleteMatchSuccessful.postValue(Event(true)) },
                        onFailure = { throw it}
                    )
                }
            }
        }
    }


    fun initLiveData() {
        viewModelScope.launch(ioDispatchers) {
            _playerLeft.postValue(null)
            _playerLeftAdjustedHandicap.postValue(-9999)
            _playerLeftScore.postValue(0)
            _playerLeftRunOut.postValue(0)

            _playerRight.postValue(null)
            _playerRightAdjustedHandicap.postValue(-9999)
            _playerRightScore.postValue(0)
            _playerRightRunOut.postValue(0)

            isMatchOver.postValue(false)
            _playerLeftAlpha.postValue(1f)
            _playerRightAlpha.postValue(1f)

            _documentReferenceId.postValue("")
            _isSetMatchSuccessful.postValue(Event(false))
        }
    }
}