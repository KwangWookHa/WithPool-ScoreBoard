package wook.pool.board.domain.usecase

import com.google.firebase.firestore.QuerySnapshot
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class GetNineBallMatchUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseRepository.getNineBallMatch(onSuccess, onFailure)
    }

}