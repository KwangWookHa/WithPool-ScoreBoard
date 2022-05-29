package wook.pool.board.screen.scoreboard

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.event.Event
import wook.pool.board.data.model.MatchPlayers
import wook.pool.board.data.model.Player
import wook.pool.board.data.model.PlayersSelectedIndex
import wook.pool.board.domain.usecase.GetPlayersUseCase
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase,
//    private val insertPlayersUseCase: InsertPlayerUseCase,
) : BaseViewModel() {

    private val _selectedHandicapIndex: MutableLiveData<PlayersSelectedIndex> = MutableLiveData()
    val selectedHandicapIndex: LiveData<PlayersSelectedIndex> = _selectedHandicapIndex

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
                    onFailure = {

                    }
                )
            }
        }
    }

    fun setPlayer(player: Player, isLeftPlayer: Boolean) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeftPlayer) {
                if (player.name == _playerRight.value?.name) {
                    _isPlayerSetSuccessful.postValue(Event(false))
                    return@launch
                }
                _playerLeft.postValue(player)
            } else {
                if (player.name == _playerLeft.value?.name) {
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

    fun switchTurn() {
        viewModelScope.launch(ioDispatchers) {
            _isTurnLeftPlayer.postValue(
                !(_isTurnLeftPlayer.value!!)
            )
        }
    }

    fun setSelectedHandicapIndex(enumValue: PlayersSelectedIndex) {
        viewModelScope.launch(ioDispatchers) {
            _selectedHandicapIndex.postValue(enumValue)
        }
    }

    fun getMatchPlayers(): MatchPlayers? =
        if (_playerLeft.value == null || _playerRight.value == null) {
            null
        } else {
            MatchPlayers(
                playerLeft = _playerLeft.value!!,
                playerRight = _playerRight.value!!,
                playerLeftAdjustedHandicap = playerLeftAdjustedHandicap.value!!,
                playerRightAdjustedHandicap = playerRightAdjustedHandicap.value!!
            )
        }


//    fun insertWithPoolPlayers() {
//        listOf(
//            Player("위드풀", "프란시스코 코로코토", 10),
//            Player("위드풀", "바다", 10),
//            Player("위드풀", "오형근", 9),
//            Player("위드풀", "이광훈", 9),
//            Player("위드풀", "남명학", 8),
//            Player("위드풀", "박준호", 8),
//            Player("위드풀", "송민규", 8),
//            Player("위드풀", "이군봉", 8),
//            Player("위드풀", "이일현", 8),
//            Player("위드풀", "이효정", 8),
//            Player("위드풀", "김강진", 7),
//            Player("위드풀", "김상현", 7),
//            Player("위드풀", "김종현", 7),
//            Player("위드풀", "김혜림", 7),
//            Player("위드풀", "송상훈", 7),
//            Player("위드풀", "서커김", 7),
//            Player("위드풀", "이기빈", 7),
//            Player("위드풀", "조훈", 7),
//            Player("위드풀", "알렉스", 7),
//            Player("위드풀", "김수", 6),
//            Player("위드풀", "류선영", 6),
//            Player("위드풀", "박희정", 6),
//            Player("위드풀", "박현", 6),
//            Player("위드풀", "서대경", 6),
//            Player("위드풀", "손대원", 6),
//            Player("위드풀", "유선임", 6),
//            Player("위드풀", "임효빈", 6),
//            Player("위드풀", "김근호", 5),
//            Player("위드풀", "방미선", 5),
//            Player("위드풀", "이경한", 5),
//            Player("위드풀", "이미", 5),
//            Player("위드풀", "이상필", 5),
//            Player("위드풀", "이진성", 5),
//            Player("위드풀", "이혜란", 5),
//            Player("위드풀", "이혜현", 5),
//            Player("위드풀", "안수형", 5),
//            Player("위드풀", "정수연", 5),
//            Player("위드풀", "박기원", 5),
//            Player("위드풀", "하광욱", 5),
//            Player("위드풀", "김은희", 4),
//            Player("위드풀", "김혜영", 4),
//            Player("위드풀", "오진숙", 4),
//            Player("위드풀", "이연수", 4),
//            Player("위드풀", "박소영", 3),
//            Player("위드풀", "이혜림(HAZEL)", 3),
//        ).let {
//            it.forEach {
//                insertPlayersUseCase(
//                    it, {}, {}
//                )
//            }
//        }
//    }
}