package wook.pool.board.data.repository

import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import javax.inject.Inject

class FirestoreRepository @Inject constructor() {

    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val COLLECTION_NINE_BALL_MATCH = "nine_ball_match"
        private const val COLLECTION_PLAYERS = "players"
        private const val FIELD_NAME = "name"
    }

    fun insertPlayer(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(COLLECTION_PLAYERS)
            .add(player)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    fun getPlayers(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        db.collection(COLLECTION_PLAYERS)
            .orderBy(FIELD_NAME, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun addNineBallMatch(
        nineBallMatch: NineBallMatch,
        onSuccess: (DocumentReference) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH)
            .add(nineBallMatch)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun updateNineBallMatch(
        documentPath: String,
        data: Map<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH)
            .document(documentPath)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    fun setNineBallMatch(
        documentPath: String,
        nineBallMatch: NineBallMatch,
        mergeFields: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH)
            .document(documentPath)
            .set(nineBallMatch, SetOptions.mergeFields(mergeFields))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)

    }

    fun getNineBallMatch(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH)
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun deleteNineBallMatch(
        documentPath: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH)
            .document(documentPath)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)

    }
}