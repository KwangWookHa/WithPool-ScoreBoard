package wook.pool.board.domain.usecase

import wook.pool.board.data.model.Player
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class InsertPlayersUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firebaseRepository.insertPlayer(player, onSuccess, onFailure)
    }

}