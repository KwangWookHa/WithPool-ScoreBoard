package wook.pool.board.domain.usecase.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wook.pool.board.data.model.Player
import wook.pool.board.data.source.remote.repository.PlayerRepository
import javax.inject.Inject

class UpdatePlayerUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    suspend fun invoke(documentId: String, updatedPlayer: Player) = withContext(Dispatchers.IO) {
        playerRepository.updatePlayer(documentId, updatedPlayer)
    }
}
