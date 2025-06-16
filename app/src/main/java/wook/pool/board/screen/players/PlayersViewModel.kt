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
import wook.pool.board.data.enums.Handicap
import wook.pool.board.domain.usecase.match.GetHeadToHeadRecordUseCase
import wook.pool.board.domain.usecase.player.GetPlayersUseCase
import wook.pool.board.domain.usecase.player.DeletePlayerUseCase
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
        private val getPlayersUseCase: GetPlayersUseCase,
        private val getHeadToHeadRecordUseCase: GetHeadToHeadRecordUseCase,
        private val deletePlayerUseCase: DeletePlayerUseCase,
) : BaseViewModel() {

    private val _selectedHandicap: MutableLiveData<Handicap> = MutableLiveData()
    val selectedHandicap: LiveData<Handicap> = _selectedHandicap

    private val _players: MutableLiveData<List<Player>> = liveData {
        emit(getPlayersUseCase.invoke())
    } as MutableLiveData<List<Player>>

    val players: LiveData<List<List<PlayerListItem>>> = Transformations.map(_players) {
        mutableListOf<List<PlayerListItem>>().apply {
            if (it.isNotEmpty()) {
                for (i in 0..10) {
                    val playerList = mutableListOf<PlayerListItem>()
                    
                    if (i < 3) {
                        // 핸디캡 0,1,2는 빈 리스트
                        add(emptyList())
                    } else {
                        // 해당 핸디캡의 플레이어들을 추가
                        it.filter { player -> player.handicap == i }
                            .forEach { player -> playerList.add(PlayerListItem.PlayerItem(player)) }
                        
                        // 마지막에 "+" 버튼 추가
                        playerList.add(PlayerListItem.AddPlayerItem)
                        
                        add(playerList)
                    }
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

    private val _deletePlayerEvent: MutableLiveData<Event<Pair<Boolean, String?>>> = MutableLiveData()
    val deletePlayerEvent: LiveData<Event<Pair<Boolean, String?>>> = _deletePlayerEvent

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
            _playerLeftDice.postValue(0)
            _playerRightDice.postValue(0)
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

    fun selectHandicap(handicap: Handicap) {
        viewModelScope.launch(ioDispatchers) {
            _selectedHandicap.postValue(handicap)
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

    fun deletePlayer(player: Player) {
        viewModelScope.launch(ioDispatchers) {
            try {
                player.documentId?.let { documentId ->
                    deletePlayerUseCase.invoke(documentId)
                    _deletePlayerEvent.postValue(Event(Pair(true, player.name)))
                    getPlayers() // 삭제 후 목록 새로고침
                }
            } catch (e: Exception) {
                Logger.e("Failed to delete player: ${e.message}")
                _deletePlayerEvent.postValue(Event(Pair(false, player.name)))
            }
        }
    }
}