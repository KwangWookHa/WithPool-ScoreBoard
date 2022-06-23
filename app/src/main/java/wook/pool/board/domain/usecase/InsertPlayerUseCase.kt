package wook.pool.board.domain.usecase

import wook.pool.board.data.model.Player
import wook.pool.board.data.repository.PlayerRepository
import javax.inject.Inject

class InsertPlayerUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    operator fun invoke(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        playerRepository.insertPlayer(player, onSuccess, onFailure)
    }
}