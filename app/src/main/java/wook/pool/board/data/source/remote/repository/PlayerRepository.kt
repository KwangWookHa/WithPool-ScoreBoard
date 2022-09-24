package wook.pool.board.data.source.remote.repository

import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import wook.pool.board.Constant
import wook.pool.board.data.model.Player
import javax.inject.Inject

class PlayerRepository @Inject constructor(
        private val fireStore: FirebaseFirestore
) {

    suspend fun insertPlayer(player: Player): DocumentReference =
            fireStore.collection(Constant.Collection.COLLECTION_PLAYERS).add(player).await()

    suspend fun getPlayers(): List<Player> =
            fireStore.collection(Constant.Collection.COLLECTION_PLAYERS)
                    .orderBy(Constant.Field.FIELD_NAME, Query.Direction.ASCENDING)
                    .get()
                    .await()
                    .toObjects(Player::class.java)
}