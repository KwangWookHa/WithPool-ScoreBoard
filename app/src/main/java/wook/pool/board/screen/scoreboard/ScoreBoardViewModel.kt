package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import wook.pool.board.BuildConfig
import wook.pool.board.data.enums.AppVersionStatus
import wook.pool.board.domain.usecase.appversion.GetAppVersionUseCase
import wook.pool.board.global.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ScoreBoardViewModel @Inject constructor(
        private val getAppVersionUseCase: GetAppVersionUseCase
) : BaseViewModel() {

    private val _navDirection: MutableLiveData<NavDirections> = MutableLiveData()
    val navDirection: LiveData<NavDirections> = _navDirection

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        signIn()
    }

    fun setNavDirection(navDirection: NavDirections) {
        _navDirection.value = navDirection
    }

    fun setLoadingProgress(value: Boolean) {
        _isLoading.value = value
    }

    private val _appVersionStatus: MutableLiveData<AppVersionStatus> = MutableLiveData()
    val appVersionStatus: LiveData<AppVersionStatus> = _appVersionStatus

    private suspend fun checkAppVersion() {
        kotlin.runCatching {
            getAppVersionUseCase.invoke()
        }.onSuccess {
            val isOutOfDate = try {
                BuildConfig.VERSION_CODE < it?.versionCode!!
            } catch (e: Exception) {
                false
            }
            _appVersionStatus.postValue(
                    if (isOutOfDate) {
                        if (it?.isImmediateUpdate == true) {
                            AppVersionStatus.UPDATE_IMMEDIATELY
                        } else {
                            AppVersionStatus.UPDATE_AVAILABLE
                        }
                    } else {
                        AppVersionStatus.UP_TO_DATE
                    }
            )
        }
    }

    private fun signIn() {
        viewModelScope.launch(ioDispatchers) {
            if (Firebase.auth.currentUser == null) {
                kotlin.runCatching {
                    Firebase.auth.signInAnonymously().await()
                }.onSuccess {
                    if (it.user != null) checkAppVersion()
                }
            } else {
                checkAppVersion()
            }
        }
    }
}