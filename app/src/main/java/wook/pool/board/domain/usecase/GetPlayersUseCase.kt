package wook.pool.board.domain.usecase

import com.google.firebase.firestore.QuerySnapshot
import wook.pool.board.data.repository.PlayerRepository
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    operator fun invoke(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        playerRepository.getPlayers(onSuccess, onFailure)
    }

}