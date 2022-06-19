package wook.pool.board.domain.usecase

import com.google.firebase.firestore.DocumentSnapshot
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(onSuccess: (DocumentSnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        firebaseRepository.getAppVersion(onSuccess, onFailure)
    }

}