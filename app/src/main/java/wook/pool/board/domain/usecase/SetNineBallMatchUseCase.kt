package wook.pool.board.domain.usecase

import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class SetNineBallMatchUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(
        documentPath: String,
        nineBallMatch: NineBallMatch,
        mergeFields: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseRepository.setNineBallMatch(documentPath, nineBallMatch, mergeFields, onSuccess, onFailure)
    }

}