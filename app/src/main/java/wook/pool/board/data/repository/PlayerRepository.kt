package wook.pool.board.data.repository

import com.google.firebase.firestore.*
import wook.pool.board.base.Constant
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    fun insertPlayer(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        fireStore.collection(Constant.Collection.COLLECTION_PLAYERS)
            .add(player)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    fun getPlayers(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        fireStore.collection(Constant.Collection.COLLECTION_PLAYERS)
            .orderBy(Constant.Field.FIELD_NAME, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }
}