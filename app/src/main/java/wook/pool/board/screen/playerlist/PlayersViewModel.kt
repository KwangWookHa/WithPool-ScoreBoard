package wook.pool.board.screen.playerlist

import androidx.lifecycle.*
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.event.Event
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.Player
import wook.pool.board.data.model.SelectedHandicapIndex
import wook.pool.board.domain.usecase.GetHeadToHeadRecordUseCase
import wook.pool.board.domain.usecase.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
        private val getPlayersUseCase: GetPlayersUseCase,
        private val getHeadToHeadRecordUseCase: GetHeadToHeadRecordUseCase,
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

    private val _isTimerMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isTimerMode: LiveData<Boolean> = _isTimerMode

    /***************************** Player Left *****************************/

    private val _playerLeft: MutableLiveData<Player?> = MutableLiveData(null)
    val playerLeft: LiveData<Player?> = _playerLeft

    private val _playerLeftDice: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftDice: LiveData<Int> = _playerLeftDice

    private val _playerLeftRecord: MutableLiveData<Triple<Int, Int, Int>> = MutableLiveData()
    val playerLeftRecord: LiveData<Triple<Int, Int, Int>> = _playerLeftRecord

    /***************************** Player Right *****************************/

    private val _playerRight: MutableLiveData<Player?> = MutableLiveData(null)
    val playerRight: LiveData<Player?> = _playerRight

    private val _playerRightDice: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightDice: LiveData<Int> = _playerRightDice

    val playerRightRecord: LiveData<Triple<Int, Int, Int>> = Transformations.map(_playerLeftRecord) {
        Triple(it.first, it.third, it.second)
    }

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

            if (_playerLeft.value != null && _playerLeft.value!!.name != "Guest"
                    && _playerRight.value != null && _playerRight.value!!.name != "Guest") {
                getHeadToHeadRecords()
            }
        }
    }

    private fun getHeadToHeadRecords() {
        viewModelScope.launch(ioDispatchers) {
            if (_playerLeft.value != null && _playerRight.value != null) {
                val matches = getHeadToHeadRecordUseCase.invoke(
                        playerLeftName = _playerLeft.value!!.name ?: return@launch,
                        playerRightName = _playerRight.value!!.name ?: return@launch,
                )
                val matchCount = matches.size
                val leftPlayerWinCount = matches.count { it.playerWinnerName == _playerLeft.value!!.name }
                val leftPlayerLoseCount = matchCount - leftPlayerWinCount
                _playerLeftRecord.postValue(Triple(matchCount, leftPlayerWinCount, leftPlayerLoseCount))
                Logger.i("matches -> $matches")
            }
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
            _playerLeftDice.postValue((1..6).random())
            _playerRightDice.postValue((1..6).random())
        }
    }

    fun initDice() {
        _playerLeftDice.value = 0
        _playerRightDice.value = 0
    }

    fun switchTimer() {
        viewModelScope.launch(ioDispatchers) {
            _isTimerMode.postValue(
                    !(_isTimerMode.value!!)
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