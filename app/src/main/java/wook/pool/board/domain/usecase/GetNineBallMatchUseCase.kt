package wook.pool.board.domain.usecase

import com.google.firebase.firestore.QuerySnapshot
import wook.pool.board.data.repository.MatchRepository
import javax.inject.Inject

class GetNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    operator fun invoke(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStoreRepository.getNineBallMatch(onSuccess, onFailure)
    }

}