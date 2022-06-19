package wook.pool.board.screen.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.BuildConfig
import wook.pool.board.base.BaseViewModel
import wook.pool.board.data.model.AppVersion
import wook.pool.board.domain.usecase.GetAppVersionUseCase
import javax.inject.Inject

@HiltViewModel
class AppVersionViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
) : BaseViewModel() {

    private val _isUpdateForced: MutableLiveData<Boolean> = MutableLiveData()
    val isUpdateForced: LiveData<Boolean> = _isUpdateForced

    init {
        viewModelScope.launch(ioDispatchers) {
            getAppVersionUseCase(
                onSuccess = {
                    val appVersion = it.toObject(AppVersion::class.java)
                    _isUpdateForced.postValue(
                        if (BuildConfig.VERSION_NAME != appVersion?.versionName) {
                            appVersion?.isImmediateUpdate
                        } else {
                            false
                        }
                    )
                },
                onFailure = { throw it }
            )
        }
    }
}