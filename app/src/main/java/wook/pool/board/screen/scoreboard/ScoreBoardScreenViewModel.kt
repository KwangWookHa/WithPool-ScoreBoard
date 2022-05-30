package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import wook.pool.board.base.BaseViewModel
import javax.inject.Inject

class ScoreBoardScreenViewModel @Inject constructor() : BaseViewModel() {

    private val _navDirection: MutableLiveData<NavDirections> = MutableLiveData()
    val navDirection: LiveData<NavDirections> = _navDirection

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setNavDirection(navDirection: NavDirections) {
        _navDirection.value = navDirection
    }

    fun setLoadingProgress(value: Boolean) {
        _isLoading.value = value
    }

}