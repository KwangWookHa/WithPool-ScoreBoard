package wook.pool.board.domain.usecase

import wook.pool.board.data.model.Player
import wook.pool.board.data.repository.FirestoreRepository
import javax.inject.Inject

class InsertPlayerUseCase @Inject constructor(
    private val firebaseRepository: FirestoreRepository,
) {

    operator fun invoke(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firebaseRepository.insertPlayer(player, onSuccess, onFailure)
    }

    fun insertWithPoolPlayers() {
        listOf(
            Player("위드풀", "프란시스코 코로코토", 10),
            Player("위드풀", "바다", 10),
            Player("위드풀", "오형근", 9),
            Player("위드풀", "이광훈", 9),
            Player("위드풀", "남명학", 8),
            Player("위드풀", "박준호", 8),
            Player("위드풀", "송민규", 8),
            Player("위드풀", "이군봉", 8),
            Player("위드풀", "이일현", 8),
            Player("위드풀", "이효정", 8),
            Player("위드풀", "김강진", 7),
            Player("위드풀", "김상현", 7),
            Player("위드풀", "김종현", 7),
            Player("위드풀", "김혜림", 7),
            Player("위드풀", "송상훈", 7),
            Player("위드풀", "서커김", 7),
            Player("위드풀", "이기빈", 7),
            Player("위드풀", "조훈", 7),
            Player("위드풀", "알렉스", 7),
            Player("위드풀", "김수", 6),
            Player("위드풀", "류선영", 6),
            Player("위드풀", "박희정", 6),
            Player("위드풀", "박현", 6),
            Player("위드풀", "서대경", 6),
            Player("위드풀", "손대원", 6),
            Player("위드풀", "유선임", 6),
            Player("위드풀", "임효빈", 6),
            Player("위드풀", "김근호", 5),
            Player("위드풀", "방미선", 5),
            Player("위드풀", "이경한", 5),
            Player("위드풀", "이미", 5),
            Player("위드풀", "이상필", 5),
            Player("위드풀", "이진성", 5),
            Player("위드풀", "이혜란", 5),
            Player("위드풀", "이혜현", 5),
            Player("위드풀", "안수형", 5),
            Player("위드풀", "정수연", 5),
            Player("위드풀", "박기원", 5),
            Player("위드풀", "하광욱", 5),
            Player("위드풀", "김은희", 4),
            Player("위드풀", "김혜영", 4),
            Player("위드풀", "오진숙", 4),
            Player("위드풀", "이연수", 4),
            Player("위드풀", "박소영", 3),
            Player("위드풀", "이혜림(HAZEL)", 3),
        ).let {
            it.forEach {
                invoke(it, {}, {})
            }
        }
    }
}