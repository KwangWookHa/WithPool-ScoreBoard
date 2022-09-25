package wook.pool.board.screen.scoreboard.pointnineball

import androidx.lifecycle.*
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.global.base.BaseViewModel
import wook.pool.board.global.extension.minus
import wook.pool.board.global.extension.plus
import wook.pool.board.data.enums.GameType
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.player.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class PointNineBallViewModel @Inject constructor(
        private val getPlayersUseCase: GetPlayersUseCase,
) : BaseViewModel() {

    private var isRunOutMode = true

    private val _handicapAdjustment: MutableLiveData<Int> = MutableLiveData(-1)
    val handicapAdjustment: LiveData<Int> = _handicapAdjustment

    private val _players: MutableLiveData<List<Player>> = MutableLiveData()

    val playersByHandicap: LiveData<MutableList<List<Player>>> = Transformations.map(_players) {
        mutableListOf<List<Player>>().apply {
            for (i in 0..10) {
                add(
                    if (i < 3) {
                        emptyList()
                    } else {
                        it.filterHandicap(i)
                    }
                )
            }
        }
    }

    /***************************** Player Left *****************************/
    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    val playerLeftHandicap: MediatorLiveData<Int> = MediatorLiveData<Int>().apply {
        this.value = 0
        addSource(_playerLeft) {
            this.value = it?.handicap?.plus(_handicapAdjustment.value!!)
        }
        addSource(_handicapAdjustment) {
            this.value = _playerLeft.value?.handicap?.plus(it)
        }
    }

    private val _playerLeftScore: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftScore: LiveData<Int?> = _playerLeftScore

    private val _playerLeftPoint: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftPoint: LiveData<Int?> = _playerLeftPoint

    private val _playerLeftPocketing: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftPocketing: LiveData<Int?> = _playerLeftPocketing

    private val _playerLeftRunOut: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftRunOut: LiveData<Int?> = _playerLeftRunOut

    private val _playerLeftTurnCount: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftTurnCount: LiveData<Int?> = _playerLeftTurnCount

    /***************************** Player Left *****************************/

    /***************************** Player Right *****************************/
    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    val playerRightHandicap: MediatorLiveData<Int> = MediatorLiveData<Int>().apply {
        this.value = 0
        addSource(_playerRight) {
            this.value = it?.handicap?.plus(_handicapAdjustment.value!!)
        }
        addSource(_handicapAdjustment) {
            this.value = _playerRight.value?.handicap?.plus(it)
        }
    }

    private val _playerRightScore: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightScore: LiveData<Int?> = _playerRightScore

    private val _playerRightPoint: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightPoint: LiveData<Int?> = _playerRightPoint

    private val _playerRightRunOut: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightRunOut: LiveData<Int?> = _playerRightRunOut

    private val _playerRightPocketing: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightPocketing: LiveData<Int?> = _playerRightPocketing

    private val _playerRightTurnCount: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightTurnCount: LiveData<Int?> = _playerRightTurnCount

    /***************************** Player Right *****************************/

    val isGameOver: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.value = false
        addSource(_playerLeftScore) {
            this.value = if (playerLeftHandicap.value == null) {
                false
            } else {
                playerLeftHandicap.value == it
            }
        }
        addSource(_playerRightScore) {
            this.value = if (playerRightHandicap.value == null) {
                false
            } else {
                playerRightHandicap.value == it
            }
        }
    }

    private val _gameType: MutableLiveData<GameType> = MutableLiveData(GameType.GAME_9_BALL)
    val gameType: LiveData<GameType> = _gameType

    private val _isTurnLeftPlayer: MutableLiveData<Boolean> = MutableLiveData(true)
    val isTurnLeftPlayer: LiveData<Boolean> = _isTurnLeftPlayer

    fun plusScore(isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                (_playerLeftScore.value!! + 1).let {
                    if (it > playerLeftHandicap.value!!) return@launch
                    _playerLeftScore.postValue(it)
                }
            } else {
                (_playerRightScore.value!! + 1).let {
                    if (it > playerRightHandicap.value!!) return@launch
                    _playerRightScore.postValue(it)
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

    fun plusPoint(isMoneyBall: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            val point = if (isMoneyBall) 2 else 1
            if (_isTurnLeftPlayer.value!!) {
                _playerLeftPoint.plus(point)
            } else {
                _playerRightPoint.plus(point)
            }
            if (isMoneyBall) plusScore(_isTurnLeftPlayer.value!!)
        }
    }

    fun switchHandicapAdjustment() {
        viewModelScope.launch(ioDispatchers) {
            _handicapAdjustment.postValue(
                when (_handicapAdjustment.value) {
                    0 -> -1
                    -1 -> -2
                    else -> 0
                }
            )
        }
    }

    fun switchTurn() {
        viewModelScope.launch(ioDispatchers) {
            _isTurnLeftPlayer.postValue(
                !(_isTurnLeftPlayer.value!!)
            )
        }
    }

    fun switchRunOutMode(mode: Boolean = true) {
        isRunOutMode = mode
        Logger.i("isRunOutMode -> $isRunOutMode")
    }

    fun plusRunOut() {
        viewModelScope.launch(ioDispatchers) {
            Logger.i("isRunOutMode -> $isRunOutMode")
            if (isRunOutMode) {
                if (_isTurnLeftPlayer.value!!) {
                    _playerLeftRunOut.plus(1)
                } else {
                    _playerRightRunOut.plus(1)
                }
            } else {
                isRunOutMode = true
            }
        }
    }

    fun plusTurnCount() {
        viewModelScope.launch(ioDispatchers) {
            if (_isTurnLeftPlayer.value!!) {
                _playerLeftTurnCount.plus(1)
            } else {
                _playerRightTurnCount.plus(1)
            }
        }
    }

    fun setPlayer(player: Player, isLeftPlayer: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeftPlayer) {
                _playerLeft.postValue(player)
            } else {
                _playerRight.postValue(player)
            }
        }
    }

    private fun List<Player>.filterHandicap(handicap: Int) =
        filter { player -> player.handicap == handicap }
}