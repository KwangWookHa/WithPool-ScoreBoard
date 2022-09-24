package wook.pool.board.domain.usecase.match

import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Inject

class SetNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    operator fun invoke(
        documentPath: String,
        nineBallMatch: NineBallMatch,
        mergeFields: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStoreRepository.setNineBallMatch(documentPath, nineBallMatch, mergeFields, onSuccess, onFailure)
    }

}