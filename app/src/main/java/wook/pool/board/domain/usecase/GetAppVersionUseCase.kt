package wook.pool.board.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wook.pool.board.data.repository.AppVersionRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val appVersionRepository: AppVersionRepository,
) {

    suspend fun invoke() = withContext(Dispatchers.IO) {
        appVersionRepository.getAppVersion()
    }

}