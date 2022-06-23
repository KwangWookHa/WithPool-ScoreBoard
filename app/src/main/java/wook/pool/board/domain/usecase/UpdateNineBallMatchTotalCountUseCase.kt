package wook.pool.board.domain.usecase

import wook.pool.board.base.Constant.Field.FIELD_COUNT
import wook.pool.board.data.repository.CountRepository
import javax.inject.Inject

class UpdateNineBallMatchTotalCountUseCase @Inject constructor(
    private val countRepository: CountRepository,
) {

    operator fun invoke(
        documentPath: String,
        variation: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        countRepository.getNineBallMatchTotalCount(
            documentPath,
            onSuccess = {
                it.get(FIELD_COUNT)?.let {
                    countRepository.updateNineBallMatchTotalCount(
                            documentPath,
                            (it as Long) + variation,
                            onSuccess,
                            onFailure
                    )
                }
            },
            onFailure = { throw it }
        )
    }
}