package wook.pool.board.domain.usecase

import wook.pool.board.data.model.NineBallMatchResult
import wook.pool.board.data.model.Player
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class InsertNineBallMatchResultUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(nineBallMatchResult: NineBallMatchResult, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firebaseRepository.insertNineBallMatchResult(nineBallMatchResult, onSuccess, onFailure)
    }

}