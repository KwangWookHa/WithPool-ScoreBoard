package wook.pool.board.screen.scoreboard

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.GetPlayersUseCase
import wook.pool.board.domain.usecase.InsertPlayerUseCase
import javax.inject.Inject

@HiltViewModel
class ScoreBoardViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val insertPlayerUseCase: InsertPlayerUseCase,
) : BaseViewModel() {

    private val _screenAction: MutableLiveData<Pair<Int, Bundle?>> = MutableLiveData()
    val screenAction: LiveData<Pair<Int, Bundle?>> = _screenAction

    private val _modeLeft: MutableLiveData<Boolean> = MutableLiveData(true)
    val modeLeft: LiveData<Boolean> = _modeLeft

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    private val _gameType: MutableLiveData<GameType> = MutableLiveData(GameType.GAME_9_BALL)
    val gameType: LiveData<GameType> = _gameType

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


    private val _isKickOffLeft: MutableLiveData<Boolean> = MutableLiveData(true)
    val isKickOffLeft: LiveData<Boolean> = _isKickOffLeft

    fun initMode(isLeft: Boolean) {
        _modeLeft.value = isLeft
    }

    fun switchKickOff() {
        _isKickOffLeft.value = !(_isKickOffLeft.value!!)
    }

    fun insertPlayer(player: Player) {
        insertPlayerUseCase(player,
            onSuccess = {},
            onFailure = {}
        )
    }

    fun getPlayers() {
        viewModelScope.launch(ioDispatchers) {
            if (_players.value == null || _players.value?.isEmpty() == true) {
                getPlayersUseCase(
                    onSuccess = {
                        val players = it.toObjects(Player::class.java)
                        _players.value = players
                    },
                    onFailure = {

                    }
                )
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

    fun setScreenAction(@IdRes navActionId: Int, bundle: Bundle? = null) {
        _screenAction.value = navActionId to bundle
    }
}