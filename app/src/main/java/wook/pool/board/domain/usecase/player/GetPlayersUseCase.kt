package wook.pool.board.domain.usecase.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wook.pool.board.data.source.remote.repository.PlayerRepository
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
        private val playerRepository: PlayerRepository,
) {

    suspend fun invoke() = withContext(Dispatchers.IO) {
        playerRepository.getPlayers()
    }

}