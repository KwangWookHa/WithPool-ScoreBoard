package wook.pool.board.domain.usecase

import com.google.firebase.firestore.DocumentSnapshot
import wook.pool.board.data.repository.AppVersionRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val appVersionRepository: AppVersionRepository,
) {

    operator fun invoke(onSuccess: (DocumentSnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        appVersionRepository.getAppVersion(onSuccess, onFailure)
    }

}