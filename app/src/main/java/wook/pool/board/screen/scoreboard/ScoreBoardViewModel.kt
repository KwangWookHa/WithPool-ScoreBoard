package wook.pool.board.screen.scoreboard

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.firestore.QuerySnapshot
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.Constant
import wook.pool.board.data.model.GameType
import wook.pool.board.data.model.Player
import wook.pool.board.data.model.PlayersDocument
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

    val playersHandicap3: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(3) }
    val playersHandicap4: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(4) }
    val playersHandicap5: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(5) }
    val playersHandicap6: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(6) }
    val playersHandicap7: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(7) }
    val playersHandicap8: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(8) }
    val playersHandicap9: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(9) }
    val playersHandicap10: LiveData<List<Player>> = Transformations.map(_players) { it.filterHandicap(10) }


    private val _isKickOffLeft: MutableLiveData<Boolean> = MutableLiveData(true)
    val isKickOffLeft: LiveData<Boolean> = _isKickOffLeft

    fun initArguments(arguments: Bundle) {
        _modeLeft.value = arguments.getBoolean(Constant.BundleKey.BUNDLE_KEY_ON_CHOICE_LEFT)
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
        getPlayersUseCase(
            onSuccess = {
                val players = it.toObjects(Player::class.java)
                _players.value = players
            },
            onFailure = {}
        )
    }

    private fun List<Player>.filterHandicap(handicap: Int) =
        filter { player -> player.handicap == handicap }

    fun setScreenAction(@IdRes navActionId: Int, bundle: Bundle? = null) {
        _screenAction.value = navActionId to bundle
    }

}