package wook.pool.board.screen.players

import androidx.lifecycle.*
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wook.pool.board.Constant
import wook.pool.board.global.base.BaseViewModel
import wook.pool.board.global.event.Event
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.Player
import wook.pool.board.data.enums.SelectedHandicapIndex
import wook.pool.board.domain.usecase.match.GetHeadToHeadRecordUseCase
import wook.pool.board.domain.usecase.player.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
        private val getPlayersUseCase: GetPlayersUseCase,
        private val getHeadToHeadRecordUseCase: GetHeadToHeadRecordUseCase,
) : BaseViewModel() {

    private val _selectedHandicapIndex: MutableLiveData<SelectedHandicapIndex> = MutableLiveData()
    val selectedHandicapIndex: LiveData<SelectedHandicapIndex> = _selectedHandicapIndex

    private val _players: MutableLiveData<List<Player>> = liveData {
        emit(getPlayersUseCase.invoke())
    } as MutableLiveData<List<Player>>

    val players: LiveData<List<List<Player>>> = Transformations.map(_players) {
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

    private val isGuestMode: Boolean
        get() = _playerLeft.value?.name == Constant.GUEST || _playerRight.value?.name == Constant.GUEST

    fun getPlayers() {
        viewModelScope.launch(ioDispatchers) {
            _players.postValue(getPlayersUseCase.invoke())
        }
    }

    fun setPlayer(player: Player, isLeft: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            val opponentPlayer = if (isLeft) _playerRight else _playerLeft
            val clickedPlayer = if (isLeft) _playerLeft else _playerRight
            if (player.name == opponentPlayer.value?.name && player.name != Constant.GUEST) {
                _isPlayerSetSuccessful.postValue(Event(false))
                return@launch
            }
            clickedPlayer.postValue(player)
            _isPlayerSetSuccessful.postValue(Event(true))
        }
    }

    fun getHeadToHeadRecords() {
        viewModelScope.launch(ioDispatchers) {
            if (_playerLeft.value != null && _playerRight.value != null && !isGuestMode) {
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
        _playerLeft.value = null
        _playerRight.value = null
        _playerLeftDice.value = 0
        _playerRightDice.value = 0
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

    fun rollDice() {
        viewModelScope.launch(ioDispatchers) {
            delay(2000L)
            _playerLeftDice.postValue((1..6).random())
            _playerRightDice.postValue((1..6).random())
        }
    }

    fun selectedHandicapIndex(
            selectedHandicapIndex: SelectedHandicapIndex = _selectedHandicapIndex.value
                    ?: SelectedHandicapIndex.INDEX_HANDICAP_5
    ) {
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