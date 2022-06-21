package wook.pool.board.domain.usecase

import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class UpdateCountUseCase @Inject constructor(
    private val fireStoreRepository: FirestoreRepository,
) {

    operator fun invoke(
        documentPath: String,
        variation: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStoreRepository.getCount(
            documentPath,
            onSuccess = {
                val countNow = it.get("count") as Int
                fireStoreRepository.updateCount(documentPath, countNow + variation, onSuccess, onFailure)
            },
            onFailure = { throw it }
        )
    }
}