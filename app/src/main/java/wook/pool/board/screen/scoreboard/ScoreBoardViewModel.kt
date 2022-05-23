package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import wook.pool.board.base.BaseViewModel
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.Player

class ScoreBoardViewModel : BaseViewModel() {

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    private val _gameType: MutableLiveData<GameType> = MutableLiveData(GameType.GAME_9_BALL)
    val gameType: LiveData<GameType> = _gameType

    private val _isKickOffLeft: MutableLiveData<Boolean> = MutableLiveData(true)
    val isKickOffLeft: LiveData<Boolean> = _isKickOffLeft

}