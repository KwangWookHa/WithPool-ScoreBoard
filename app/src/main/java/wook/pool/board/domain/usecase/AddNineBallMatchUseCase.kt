package wook.pool.board.domain.usecase

import com.google.firebase.firestore.DocumentReference
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class AddNineBallMatchUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(nineBallMatch: NineBallMatch, onSuccess: (DocumentReference) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseRepository.addNineBallMatch(nineBallMatch, onSuccess, onFailure)
    }

}