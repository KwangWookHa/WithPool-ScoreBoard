package wook.pool.board.domain.usecase.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wook.pool.board.data.source.remote.repository.PlayerRepository
import javax.inject.Inject

class DeletePlayerUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    suspend fun invoke(documentId: String) = withContext(Dispatchers.IO) {
        playerRepository.deletePlayer(documentId)
    }
}
