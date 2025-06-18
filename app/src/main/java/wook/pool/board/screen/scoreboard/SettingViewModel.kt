package wook.pool.board.screen.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wook.pool.board.domain.usecase.table.CheckTableNumberSetUseCase
import wook.pool.board.domain.usecase.table.GetTableNumberUseCase
import wook.pool.board.domain.usecase.table.SetTableNumberUseCase
import wook.pool.board.global.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getTableNumberUseCase: GetTableNumberUseCase,
    private val setTableNumberUseCase: SetTableNumberUseCase,
    private val checkTableNumberSetUseCase: CheckTableNumberSetUseCase
) : BaseViewModel() {

    private val _selectedTableNumber = MutableLiveData<Int>()
    val selectedTableNumber: LiveData<Int> = _selectedTableNumber

    init {
        loadCurrentTableNumber()
    }

    private fun loadCurrentTableNumber() {
        viewModelScope.launch(ioDispatchers) {
            val tableNumber = if (checkTableNumberSetUseCase()) {
                getTableNumberUseCase()
            } else {
                1 // 기본값
            }
            _selectedTableNumber.postValue(tableNumber)
        }
    }

    fun setTableNumber(tableNumber: Int) {
        viewModelScope.launch(ioDispatchers) {
            setTableNumberUseCase(tableNumber)
            _selectedTableNumber.postValue(tableNumber)
        }
    }

    fun getCurrentTableNumber(): Int {
        // LiveData에 값이 있으면 사용, 없으면 직접 UseCase 호출
        return _selectedTableNumber.value ?: run {
            val tableNumber = if (checkTableNumberSetUseCase()) {
                getTableNumberUseCase()
            } else {
                1 // 기본값
            }
            _selectedTableNumber.value = tableNumber
            tableNumber
        }
    }
}
