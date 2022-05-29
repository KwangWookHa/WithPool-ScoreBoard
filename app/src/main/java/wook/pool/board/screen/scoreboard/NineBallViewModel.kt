package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.minus
import wook.pool.board.base.plus
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class NineBallViewModel @Inject constructor() : BaseViewModel() {

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

    val isGameOver: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.value = false
        addSource(_playerLeftScore) {
            if (_playerLeftAdjustedHandicap.value == null) return@addSource
            this.value = if (_playerLeftAdjustedHandicap.value!! == it) {
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
                _playerLeftAlpha.postValue(0.4f)
                true
            } else {
                _playerLeftAlpha.postValue(1f)
                false
            }
        }
    }


    private val _gameType: MutableLiveData<GameType> = MutableLiveData(GameType.GAME_9_BALL)
    val gameType: LiveData<GameType> = _gameType

    fun initMatchPlayers(matchPlayers: MatchPlayers?) {
        matchPlayers?.let {
            _playerLeft.value = it.playerLeft
            _playerRight.value = it.playerRight
            _playerLeftAdjustedHandicap.value = it.playerLeftAdjustedHandicap
            _playerRightAdjustedHandicap.value = it.playerRightAdjustedHandicap
        }
    }

    fun plusScore(isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isGameOver.value != true) {
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
            if (isLeft) {
                _playerLeftRunOut.plus(1)
            } else {
                _playerRightRunOut.plus(1)
            }
        }
    }
}