package wook.pool.board.domain.usecase

import wook.pool.board.data.repository.MatchRepository
import javax.inject.Inject

class DeleteNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    operator fun invoke(
        documentPath: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStoreRepository.deleteNineBallMatch(documentPath, onSuccess, onFailure)
    }

}