package wook.pool.board.domain.usecase

import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class UpdateNineBallMatchUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(
        documentPath: String,
        data: Map<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseRepository.updateNineBallMatch(documentPath, data, onSuccess, onFailure)
    }

}