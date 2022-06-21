package wook.pool.board.domain.usecase

import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class UpdateNineBallMatchUseCase @Inject constructor(
    private val fireStoreRepository: FirestoreRepository,
) {

    operator fun invoke(
        documentPath: String,
        data: Map<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStoreRepository.updateNineBallMatch(documentPath, data, onSuccess, onFailure)
    }

}