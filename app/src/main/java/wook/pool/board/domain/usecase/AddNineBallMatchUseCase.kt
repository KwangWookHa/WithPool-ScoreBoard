package wook.pool.board.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Inject

class AddNineBallMatchUseCase @Inject constructor(
        private val fireStoreRepository: MatchRepository,
) {

    suspend fun invoke(nineBallMatch: NineBallMatch) = withContext(Dispatchers.IO) {
        fireStoreRepository.addNineBallMatch(nineBallMatch)
    }

}