package wook.pool.board.domain.usecase.match

import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Inject

class UpdateNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    suspend fun invoke(
        documentPath: String,
        data: Map<String, Any>
    ) {
        fireStoreRepository.updateNineBallMatch(documentPath, data)
    }

}