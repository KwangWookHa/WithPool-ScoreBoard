package wook.pool.board.domain.usecase

import wook.pool.board.data.model.Player
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class InsertPlayerUseCase @Inject constructor(
    private val fireStoreRepository: FirestoreRepository,
) {

    operator fun invoke(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        fireStoreRepository.insertPlayer(player, onSuccess, onFailure)
    }
}