package wook.pool.board.domain.usecase.table

import wook.pool.board.util.PreferencesManager
import javax.inject.Inject

class SetTableNumberUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    
    operator fun invoke(tableNumber: Int) {
        preferencesManager.setTableNumber(tableNumber)
    }
}
