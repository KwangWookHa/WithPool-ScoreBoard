package wook.pool.board.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Inject

class GetHeadToHeadRecordUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    suspend fun invoke(
            playerLeftName: String,
            playerRightName: String,
    ) = withContext(Dispatchers.IO) {
        mutableListOf<NineBallMatch>().apply {
            val (result1, result2) = awaitAll(
                    async { fireStoreRepository.getHeadToHeadRecords(playerLeftName, playerRightName) },
                    async { fireStoreRepository.getHeadToHeadRecords(playerRightName, playerLeftName) }
            )
            addAll(result1)
            addAll(result2)
        }
    }
}