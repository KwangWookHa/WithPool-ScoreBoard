package wook.pool.board.domain.usecase

import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Inject

class UpdateNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
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