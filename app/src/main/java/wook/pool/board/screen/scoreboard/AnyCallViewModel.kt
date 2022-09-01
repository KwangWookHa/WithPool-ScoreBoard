package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wook.pool.board.base.BaseViewModel
import wook.pool.board.base.plus
import javax.inject.Inject

@HiltViewModel
class AnyCallViewModel @Inject constructor() : BaseViewModel() {

    private val _playerLeftScore: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftScore: LiveData<Int> = _playerLeftScore

    private val _playerLeftTotal: MutableLiveData<Int> = MutableLiveData(0)
    val playerLeftTotal: LiveData<Int> = _playerLeftTotal

    private val _playerLeftAlpha: MutableLiveData<Float> = MutableLiveData(1f)
    val playerLeftAlpha: LiveData<Float> = _playerLeftAlpha

    private val _playerLeftDice: MutableLiveData<Int> = MutableLiveData()
    val playerLeftDice: LiveData<Int> = _playerLeftDice


    private val _playerRightScore: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightScore: LiveData<Int> = _playerRightScore

    private val _playerRightTotal: MutableLiveData<Int> = MutableLiveData(0)
    val playerRightTotal: LiveData<Int> = _playerRightTotal

    private val _playerRightAlpha: MutableLiveData<Float> = MutableLiveData(1f)
    val playerRightAlpha: LiveData<Float> = _playerRightAlpha

    private val _playerRightDice: MutableLiveData<Int> = MutableLiveData()
    val playerRightDice: LiveData<Int> = _playerRightDice


    private val _isPlayerLeftWinner: MutableLiveData<Boolean> = MutableLiveData()
    val isPlayerLeftWinner: LiveData<Boolean> = _isPlayerLeftWinner

    val isMatchOver: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.value = false
        addSource(_playerLeftScore) {
            this.value = if (_playerLeftTotal.value!! != 0 && _playerLeftTotal.value!! == it) {
                _isPlayerLeftWinner.postValue(true)
                _playerRightAlpha.postValue(0.4f)
                true
            } else {
                _playerRightAlpha.postValue(1f)
                false
            }
        }
        addSource(_playerRightScore) {
            this.value = if (_playerRightTotal.value!! != 0 && _playerRightTotal.value!! == it) {
                _isPlayerLeftWinner.postValue(false)
                _playerLeftAlpha.postValue(0.4f)
                true
            } else {
                _playerLeftAlpha.postValue(1f)
                false
            }
        }
    }

    fun setScore(isLeft: Boolean, variation: Int) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                if (_playerLeftScore.value!! + variation > _playerLeftTotal.value!!) return@launch
                if (variation < 0 && _playerLeftScore.value!! <= 0) return@launch
                _playerLeftScore.plus(variation)
            } else {
                if (_playerRightScore.value!! + variation > _playerRightTotal.value!!) return@launch
                if (variation < 0 && _playerRightScore.value!! <= 0) return@launch
                _playerRightScore.plus(variation)
            }
        }
    }

    fun setTotal(isLeft: Boolean, input: Int) {
        viewModelScope.launch(ioDispatchers) {
            if (isLeft) {
                _playerLeftTotal.postValue(input)
            } else {
                _playerRightTotal.postValue(input)
            }
        }
    }

    fun randomizeDice() {
        viewModelScope.launch(ioDispatchers) {
            delay(2000L)
            _playerLeftDice.postValue((1..6).random())
            _playerRightDice.postValue((1..6).random())
        }
    }

    fun initLiveData() {
        viewModelScope.launch(ioDispatchers) {
            _playerLeftScore.postValue(0)
            _playerLeftTotal.postValue(0)
            _playerLeftDice.postValue(0)

            _playerRightScore.postValue(0)
            _playerRightTotal.postValue(0)
            _playerRightDice.postValue(0)

            _playerLeftAlpha.postValue(1f)
            _playerRightAlpha.postValue(1f)
            isMatchOver.postValue(false)
        }
    }
}