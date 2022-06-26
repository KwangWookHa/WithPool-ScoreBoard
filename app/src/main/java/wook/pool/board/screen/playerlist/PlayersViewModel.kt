package wook.pool.board.screen.playerlist

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.event.Event
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.Player
import wook.pool.board.data.model.SelectedHandicapIndex
import wook.pool.board.domain.usecase.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
        private val getPlayersUseCase: GetPlayersUseCase,
//    private val insertPlayersUseCase: InsertPlayerUseCase,
) : BaseViewModel() {

    private val _selectedHandicapIndex: MutableLiveData<SelectedHandicapIndex> = MutableLiveData()
    val selectedHandicapIndex: LiveData<SelectedHandicapIndex> = _selectedHandicapIndex

    private val _players: MutableLiveData<List<Player>> = MutableLiveData()
    val playersByHandicap: LiveData<MutableList<List<Player>>> = Transformations.map(_players) {
        mutableListOf<List<Player>>().apply {
            if (it.isNotEmpty()) {
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
            if (it == null) {
                this.value = 0
                return@addSource
            }
            this.value = it.handicap?.plus(_handicapAdjustment.value!!)
        }
        addSource(_handicapAdjustment) {
            if (it == null) {
                this.value = 0
                return@addSource
            }
            this.value = _playerLeft.value?.handicap?.plus(it)
        }
    }

    private val _playerLeftDice: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val playerLeftDice: LiveData<Pair<Int, Int>> = _playerLeftDice

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

    private val _playerRightDice: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val playerRightDice: LiveData<Pair<Int, Int>> = _playerRightDice

    /***************************** Player Right *****************************/

    private val _isPlayerSetSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isPlayerSetSuccessful: LiveData<Event<Boolean>> = _isPlayerSetSuccessful

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
                        onFailure = { throw it }
                )
            }
        }
    }

    fun setPlayer(player: Player, isLeftPlayer: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeftPlayer) {
                if (player.name == _playerRight.value?.name && player.name != "Guest") {
                    _isPlayerSetSuccessful.postValue(Event(false))
                    return@launch
                }
                _playerLeft.postValue(player)
            } else {
                if (player.name == _playerLeft.value?.name && player.name != "Guest") {
                    _isPlayerSetSuccessful.postValue(Event(false))
                    return@launch
                }
                _playerRight.postValue(player)
            }
            _isPlayerSetSuccessful.postValue(Event(true))
        }
    }

    fun initPlayers() {
        viewModelScope.launch(ioDispatchers) {
            _playerLeft.postValue(null)
            _playerRight.postValue(null)
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

    fun randomizeDice() {
        viewModelScope.launch(ioDispatchers) {
            delay(2000L)
            _playerLeftDice.postValue(Pair(getDiceValue(), getDiceValue()))
            _playerRightDice.postValue(Pair(getDiceValue(), getDiceValue()))
        }
    }

    fun initDice() {
        _playerLeftDice.value = Pair(0, 0)
        _playerRightDice.value = Pair(0, 0)
    }

    private fun getDiceValue(): Int = (1..6).random()

    fun switchTurn() {
        viewModelScope.launch(ioDispatchers) {
            _isTurnLeftPlayer.postValue(
                    !(_isTurnLeftPlayer.value!!)
            )
        }
    }

    fun setSelectedHandicapIndex(selectedHandicapIndex: SelectedHandicapIndex) {
        viewModelScope.launch(ioDispatchers) {
            _selectedHandicapIndex.postValue(selectedHandicapIndex)
        }
    }

    fun getMatchPlayers(): MatchPlayers? =
            if (_playerLeft.value == null || _playerRight.value == null) {
                null
            } else {
                MatchPlayers(
                        playerLeft = _playerLeft.value!!,
                        playerRight = _playerRight.value!!,
                        adjustment = _handicapAdjustment.value!!,
                )
            }
}