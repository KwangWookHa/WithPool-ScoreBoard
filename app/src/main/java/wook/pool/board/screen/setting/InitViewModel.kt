package wook.pool.board.screen.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.BuildConfig
import wook.pool.board.base.BaseViewModel
import wook.pool.board.data.model.AppVersion
import wook.pool.board.domain.usecase.GetAppVersionUseCase
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
        private val getAppVersionUseCase: GetAppVersionUseCase,
) : BaseViewModel() {

    private val _isUpdateForced: MutableLiveData<Boolean> = MutableLiveData()
    val isUpdateForced: LiveData<Boolean> = _isUpdateForced

    private val _isUpdateAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val isUpdateAvailable: LiveData<Boolean> = _isUpdateAvailable

    private val _isUpToDateVersion: MutableLiveData<Boolean> = MutableLiveData()
    val isUpToDateVersion: LiveData<Boolean> = _isUpToDateVersion

    init {
        viewModelScope.launch(ioDispatchers) {
            getAppVersionUseCase(
                    onSuccess = {
                        val appVersion = it.toObject(AppVersion::class.java)
                        if (BuildConfig.VERSION_NAME != appVersion?.versionName) {
                            if (appVersion?.isImmediateUpdate == true) {
                                _isUpdateForced.postValue(true)
                            } else {
                                _isUpdateAvailable.postValue(true)
                            }
                        } else {
                            _isUpToDateVersion.postValue(true)
                        }
                    },
                    onFailure = { throw it }
            )
        }
    }

    fun signInAnonymously() {
        if (Firebase.auth.currentUser == null) {
            Firebase.auth.signInAnonymously()
        }
    }
}