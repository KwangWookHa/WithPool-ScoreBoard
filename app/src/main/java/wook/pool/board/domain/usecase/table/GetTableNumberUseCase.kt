package wook.pool.board.domain.usecase.table

import wook.pool.board.util.PreferencesManager
import javax.inject.Inject

class GetTableNumberUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    
    operator fun invoke(): Int {
        return preferencesManager.getTableNumber()
    }
}
