package wook.pool.board.screen.scoreboard.nineball

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.Constant
import wook.pool.board.Constant.GUEST
import wook.pool.board.data.enums.GameType
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.match.AddNineBallMatchUseCase
import wook.pool.board.domain.usecase.match.DeleteNineBallMatchUseCase
import wook.pool.board.domain.usecase.match.UpdateNineBallMatchUseCase
import wook.pool.board.global.base.BaseViewModel
import wook.pool.board.global.base.ScoreLiveData
import wook.pool.board.global.event.Event
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

    private val _playerLeftAdjustedHandicap: ScoreLiveData = ScoreLiveData(Constant.IS_NOT_INITIALIZED)
    val playerLeftAdjustedHandicap: LiveData<Int> = _playerLeftAdjustedHandicap

    private val _playerLeftScore: ScoreLiveData = ScoreLiveData()
    val playerLeftScore: LiveData<Int> = _playerLeftScore

    private val _playerLeftRunOut: ScoreLiveData = ScoreLiveData()
    val playerLeftRunOut: LiveData<Int> = _playerLeftRunOut

    private val _playerLeftAlpha: MutableLiveData<Float> = MutableLiveData(1f)
    val playerLeftAlpha: LiveData<Float> = _playerLeftAlpha

    /***************************** Player Right *****************************/

    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    private val _playerRightAdjustedHandicap: ScoreLiveData = ScoreLiveData(Constant.IS_NOT_INITIALIZED)
    val playerRightAdjustedHandicap: LiveData<Int> = _playerRightAdjustedHandicap

    private val _playerRightScore: ScoreLiveData = ScoreLiveData()
    val playerRightScore: LiveData<Int> = _playerRightScore

    private val _playerRightRunOut: ScoreLiveData = ScoreLiveData()
    val playerRightRunOut: LiveData<Int> = _playerRightRunOut

    private val _playerRightAlpha: MutableLiveData<Float> = MutableLiveData(1f)
    val playerRightAlpha: LiveData<Float> = _playerRightAlpha

    /***************************** Player Right *****************************/

    val isGuestMode: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.value = false
        addSource(_playerLeft) { this.value = (it?.name == GUEST || _playerRight.value?.name == GUEST) }
        addSource(_playerRight) { this.value = (it?.name == GUEST || _playerLeft.value?.name == GUEST) }
    }
    val isGuestModeValue: Boolean get() = isGuestMode.value == true


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

    fun initMatch(matchPlayers: MatchPlayers?) {
        viewModelScope.launch(ioDispatchers) {
            matchPlayers?.let {
                _playerLeft.postValue(it.playerLeft)
                _playerRight.postValue(it.playerRight)
                _playerLeftAdjustedHandicap.postValue(it.playerLeft.handicap?.plus(it.adjustment))
                _playerRightAdjustedHandicap.postValue(it.playerRight.handicap?.plus(it.adjustment))
                if (it.playerLeft.name != GUEST && it.playerRight.name != GUEST) {
                    initNineBallMatch(it.playerLeft, it.playerRight, it.adjustment)
                }
            }
        }
    }

    fun setScore(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            val score = if (isLeft) _playerLeftScore else _playerRightScore
            val handicap = if (isLeft) _playerLeftAdjustedHandicap else _playerRightAdjustedHandicap
            val field = if (isLeft) Constant.Field.FILED_PLAYER_LEFT_SCORE else Constant.Field.FILED_PLAYER_RIGHT_SCORE
            if (score.value!! + variation > handicap.value!!) return@launch

            _documentPath.value?.let {
                if (it.isNotBlank() && !isGuestModeValue) {
                    kotlin.runCatching {
                        updateNineBallMatchUseCase.invoke(
                                documentPath = it,
                                data = mapOf(field to score.value!! + variation)
                        )
                    }.onSuccess {
                        score.plus(variation)
                        return@launch
                    }
                }
            }
            score.plus(variation)
        }
    }

    fun setRunOut(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            val score = if (isLeft) _playerLeftScore else _playerRightScore
            val handicap = if (isLeft) _playerLeftAdjustedHandicap else _playerRightAdjustedHandicap
            val runOut = if (isLeft) _playerLeftRunOut else _playerRightRunOut

            val scoreField = if (isLeft) Constant.Field.FILED_PLAYER_LEFT_SCORE else Constant.Field.FILED_PLAYER_RIGHT_SCORE
            val runOutField = if (isLeft) Constant.Field.FILED_PLAYER_LEFT_RUN_OUT else Constant.Field.FILED_PLAYER_RIGHT_RUN_OUT
            if (score.value!! + variation > handicap.value!!) return@launch

            _documentPath.value?.let {
                if (it.isNotBlank() && !isGuestModeValue) {
                    kotlin.runCatching {
                        updateNineBallMatchUseCase.invoke(
                                documentPath = it,
                                data = hashMapOf<String, Int>().apply {
                                    put(scoreField, score.value!! + variation)
                                    put(runOutField, runOut.value!! + variation)
                                }
                        )
                    }.onSuccess {
                        score.plus(variation)
                        runOut.plus(variation)
                        return@launch
                    }
                }
            }
            score.plus(variation)
            runOut.plus(variation)
        }
    }

    private fun initNineBallMatch(playerLeft: Player, playerRight: Player, adjustment: Int) {
        if (playerLeft.name == GUEST || playerRight.name == GUEST) {
            return // Guest 모드면 DB 저장 안함
        }
        viewModelScope.launch(ioDispatchers) {
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
                if (it.isNotBlank() && isMatchOver.value!! && !isGuestModeValue) {
                    val winnerName = (if (_isPlayerLeftWinner.value!!) _playerLeft.value?.name else _playerRight.value?.name)
                            ?: ""
                    val loserName = (if (_isPlayerLeftWinner.value!!) _playerRight.value?.name else _playerLeft.value?.name)
                            ?: ""
                    kotlin.runCatching {
                        updateNineBallMatchUseCase.invoke(
                                documentPath = it,
                                data = hashMapOf<String, Any>().apply {
                                    put(Constant.Field.FILED_IS_LIVE, false)
                                    put(Constant.Field.FILED_PLAYER_WINNER_NAME, winnerName)
                                    put(Constant.Field.FILED_PLAYER_LOSER_NAME, loserName)
                                    put(Constant.Field.FILED_END_TIME_STAMP, Timestamp.now())
                                }
                        )
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
                if (it.isNotBlank() && !isGuestModeValue) {
                    kotlin.runCatching {
                        deleteNineBallMatchUseCase.invoke(it)
                    }.onSuccess {
                        _isDeleteMatchSuccessful.postValue(Event(true))
                        return@launch
                    }
                }
            }
            _isDeleteMatchSuccessful.postValue(Event(true))
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
            _isFinishMatchSuccessful.postValue(Event(false))
        }
    }
}