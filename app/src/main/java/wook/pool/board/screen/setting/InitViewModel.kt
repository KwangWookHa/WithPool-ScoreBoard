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
import wook.pool.board.domain.usecase.GetAppVersionUseCase
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
        private val getAppVersionUseCase: GetAppVersionUseCase,
) : BaseViewModel() {

    private val _isSignInSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    val isSignInSuccessful: LiveData<Boolean> = _isSignInSuccessful

    private val _isImmediateUpdate: MutableLiveData<Boolean> = MutableLiveData()
    val isImmediateUpdate: LiveData<Boolean> = _isImmediateUpdate

    private val _isUpdateAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val isUpdateAvailable: LiveData<Boolean> = _isUpdateAvailable

    private val _isUpToDateVersion: MutableLiveData<Boolean> = MutableLiveData()
    val isUpToDateVersion: LiveData<Boolean> = _isUpToDateVersion

    fun checkAppVersion() {
        viewModelScope.launch(ioDispatchers) {
            kotlin.runCatching {
                getAppVersionUseCase.invoke()
            }.onSuccess {
                if (BuildConfig.VERSION_NAME != it?.versionName) {
                    if (it?.isImmediateUpdate == true) {
                        _isImmediateUpdate.postValue(true)
                    } else {
                        _isUpdateAvailable.postValue(true)
                    }
                } else {
                    _isUpToDateVersion.postValue(true)
                }
            }
        }
    }

    fun signInAnonymously() {
        viewModelScope.launch(ioDispatchers) {
            if (Firebase.auth.currentUser == null) {
                Firebase.auth.signInAnonymously().addOnSuccessListener {
                    _isSignInSuccessful.postValue(it.user != null)
                }
            } else {
                _isSignInSuccessful.postValue(true)
            }
        }
    }
}