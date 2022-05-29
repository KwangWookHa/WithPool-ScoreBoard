package wook.pool.board.screen.scoreboard

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import wook.pool.board.base.BaseViewModel
import javax.inject.Inject

class ScoreBoardScreenViewModel @Inject constructor() : BaseViewModel() {

    private val _navDirection: MutableLiveData<NavDirections> = MutableLiveData()
    val navDirection: LiveData<NavDirections> = _navDirection

    fun setNavDirection(navDirection: NavDirections) {
        _navDirection.value = navDirection
    }

}