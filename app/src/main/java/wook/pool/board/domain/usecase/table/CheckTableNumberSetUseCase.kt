package wook.pool.board.domain.usecase.table

import wook.pool.board.util.PreferencesManager
import javax.inject.Inject

class CheckTableNumberSetUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    
    operator fun invoke(): Boolean {
        return preferencesManager.isTableNumberSet()
    }
}
