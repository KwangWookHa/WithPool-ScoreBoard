package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.Constant
import wook.pool.board.base.event.Event
import wook.pool.board.base.plus
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.AddNineBallMatchUseCase
import wook.pool.board.domain.usecase.DeleteNineBallMatchUseCase
import wook.pool.board.domain.usecase.SetNineBallMatchUseCase
import wook.pool.board.domain.usecase.UpdateNineBallMatchUseCase
import javax.inject.Inject

@HiltViewModel
class NineBallViewModel @Inject constructor(
    private val addNineBallMatchUseCase: AddNineBallMatchUseCase,
    private val setNineBallMatchUseCase: SetNineBallMatchUseCase,
    private val updateNineBallMatchUseCase: UpdateNineBallMatchUseCase,
    private val deleteNineBallMatchUseCase: DeleteNineBallMatchUseCase,
) : BaseViewModel() {

    private lateinit var startTimeStamp: Timestamp

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

    fun setScore(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                if (_playerLeftScore.value!! + variation > _playerLeftAdjustedHandicap.value!!) return@launch
            } else {
                if (_playerRightScore.value!! + variation > _playerRightAdjustedHandicap.value!!) return@launch
            }

            _documentPath.value?.let {
                if (it.isNotBlank()) {
                    updateNineBallMatchUseCase(
                        documentPath = it,
                        data = mapOf(
                            if (isLeft) {
                                Constant.Field.FILED_PLAYER_LEFT_SCORE to _playerLeftScore.value!! + variation
                            } else {
                                Constant.Field.FILED_PLAYER_RIGHT_SCORE to _playerRightScore.value!! + variation
                            }
                        ),
                        onSuccess = {
                            if (isLeft) {
                                _playerLeftScore.plus(variation)
                            } else {
                                _playerRightScore.plus(variation)
                            }
                        },
                        onFailure = { throw it }
                    )
                }
            }
        }
    }

    fun setRunOut(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            _documentPath.value?.let {
                if (isLeft) {
                    if (_playerLeftScore.value!! + variation > _playerLeftAdjustedHandicap.value!!) return@launch
                } else {
                    if (_playerRightScore.value!! + variation > _playerRightAdjustedHandicap.value!!) return@launch
                }

                if (it.isNotBlank()) {
                    updateNineBallMatchUseCase(
                        documentPath = it,
                        data = hashMapOf<String, Int>().apply {
                            if (isLeft) {
                                put(Constant.Field.FILED_PLAYER_LEFT_SCORE, _playerLeftScore.value!! + variation)
                                put(Constant.Field.FILED_PLAYER_LEFT_RUN_OUT, _playerLeftRunOut.value!! + variation)
                            } else {
                                put(Constant.Field.FILED_PLAYER_RIGHT_SCORE, _playerRightScore.value!! + variation)
                                put(Constant.Field.FILED_PLAYER_RIGHT_RUN_OUT, _playerRightRunOut.value!! + variation)
                            }
                        },
                        onSuccess = {
                            if (isLeft) {
                                _playerLeftRunOut.plus(1)
                                _playerLeftScore.plus(variation)
                            } else {
                                _playerRightRunOut.plus(1)
                                _playerRightScore.plus(variation)
                            }
                        },
                        onFailure = { throw it }
                    )
                }
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
                    matchStartTimeStamp = startTimeStamp,
                    matchEndTimeStamp = null,
                ),
                onSuccess = { _documentPath.postValue(it.id) },
                onFailure = { throw it }
            )
        }
    }

    fun finishNineBallMatch() {
        viewModelScope.launch(ioDispatchers) {
            _documentPath.value?.let {
                if (it.isNotBlank() && isMatchOver.value!!) {
                    setNineBallMatchUseCase(
                        documentPath = it,
                        nineBallMatch = NineBallMatch(
                            isLive = false,
                            playerWinnerName = if (_isPlayerLeftWinner.value!!) _playerLeft.value?.name else _playerRight.value?.name,
                            playerLoserName = if (_isPlayerLeftWinner.value!!) _playerRight.value?.name else _playerLeft.value?.name,
                            matchEndTimeStamp = Timestamp.now(),
                        ),
                        mergeFields = listOf(
                            Constant.Field.FILED_IS_LIVE,
                            Constant.Field.FILED_PLAYER_WINNER_NAME,
                            Constant.Field.FILED_PLAYER_LOSER_NAME,
                            Constant.Field.FILED_END_TIME_STAMP
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
            _documentPath.value?.let {
                if (it.isNotBlank()) {
                    deleteNineBallMatchUseCase(
                        documentPath = it,
                        onSuccess = { _isDeleteMatchSuccessful.postValue(Event(true)) },
                        onFailure = { throw it }
                    )
                }
            }
        }
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
            _isSetMatchSuccessful.postValue(Event(false))
        }
    }
}