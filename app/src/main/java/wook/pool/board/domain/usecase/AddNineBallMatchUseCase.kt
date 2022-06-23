package wook.pool.board.domain.usecase

import com.google.firebase.firestore.DocumentReference
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.repository.MatchRepository
import javax.inject.Inject

class AddNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    operator fun invoke(nineBallMatch: NineBallMatch, onSuccess: (DocumentReference) -> Unit, onFailure: (Exception) -> Unit) {
        fireStoreRepository.addNineBallMatch(nineBallMatch, onSuccess, onFailure)
    }

}