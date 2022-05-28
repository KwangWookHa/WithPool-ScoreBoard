package wook.pool.board.screen.scoreboard

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.Player
import wook.pool.board.domain.usecase.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase,
) : BaseViewModel() {

    private val _players: MutableLiveData<List<Player>> = MutableLiveData()

    val playersByHandicap: LiveData<MutableList<List<Player>>> = Transformations.map(_players) {
        mutableListOf<List<Player>>().apply {
            for (i in 0..10) {
                add(
                    if (i < 3) {
                        emptyList()
                    } else {
                        it.filter { player -> player.handicap == i }
                    }
                )
            }
        }
    }

    private val _handicapAdjustment: MutableLiveData<Int> = MutableLiveData(-1)
    val handicapAdjustment: LiveData<Int> = _handicapAdjustment

    private val _isTurnLeftPlayer: MutableLiveData<Boolean> = MutableLiveData(true)
    val isTurnLeftPlayer: LiveData<Boolean> = _isTurnLeftPlayer

    /***************************** Player Left *****************************/

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    val playerLeftAdjustedHandicap: MediatorLiveData<Int> = MediatorLiveData<Int>().apply {
        this.value = 0
        addSource(_playerLeft) {
            this.value = it?.handicap?.plus(_handicapAdjustment.value!!)
        }
        addSource(_handicapAdjustment) {
            this.value = _playerLeft.value?.handicap?.plus(it)
        }
    }

    /***************************** Player Right *****************************/

    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    val playerRightAdjustedHandicap: MediatorLiveData<Int> = MediatorLiveData<Int>().apply {
        this.value = 0
        addSource(_playerRight) {
            this.value = it?.handicap?.plus(_handicapAdjustment.value!!)
        }
        addSource(_handicapAdjustment) {
            this.value = _playerRight.value?.handicap?.plus(it)
        }
    }

    init {
        getPlayers()
    }

    private fun getPlayers() {
        viewModelScope.launch(ioDispatchers) {
            if (_players.value == null || _players.value?.isEmpty() == true) {
                getPlayersUseCase(
                    onSuccess = {
                        val players = it.toObjects(Player::class.java)
                        _players.postValue(players)
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

    fun getMatchPlayers() =
        MatchPlayers(
            playerLeft = _playerLeft.value!!,
            playerRight = _playerRight.value!!,
            playerLeftAdjustedHandicap = playerLeftAdjustedHandicap.value!!,
            playerRightAdjustedHandicap = playerRightAdjustedHandicap.value!!
        )
}