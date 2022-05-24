package wook.pool.board.domain.usecase

import com.google.firebase.firestore.QuerySnapshot
import wook.pool.board.data.model.Player
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        firebaseRepository.getPlayers(onSuccess, onFailure)
    }

}