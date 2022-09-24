package wook.pool.board.domain.usecase.match

import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Inject

class DeleteNineBallMatchUseCase @Inject constructor(
        private val matchRepository: MatchRepository,
) {

    suspend operator fun invoke(documentPath: String) {
        matchRepository.deleteNineBallMatch(documentPath)
    }

}