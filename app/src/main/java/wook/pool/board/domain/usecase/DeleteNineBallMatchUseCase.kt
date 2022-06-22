package wook.pool.board.domain.usecase

import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class DeleteNineBallMatchUseCase @Inject constructor(
    private val fireStoreRepository: FirestoreRepository,
) {

    operator fun invoke(
        documentPath: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStoreRepository.deleteNineBallMatch(documentPath, onSuccess, onFailure)
    }

}