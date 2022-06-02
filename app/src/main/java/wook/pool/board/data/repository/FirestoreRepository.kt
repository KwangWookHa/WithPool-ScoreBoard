package wook.pool.board.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import javax.inject.Inject

class FirestoreRepository @Inject constructor() {

    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val COLLECTION_NINE_BALL_MATCH_RESULT = "nine_ball_match"
        private const val COLLECTION_PLAYERS = "players"
    }

    fun insertPlayer(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(COLLECTION_PLAYERS)
            .add(player)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getPlayers(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        db.collection(COLLECTION_PLAYERS)
            .get()
            .addOnSuccessListener { onSuccess(it) }
            .addOnFailureListener { onFailure(it) }
    }

    fun addNineBallMatch(
        nineBallMatch: NineBallMatch,
        onSuccess: (DocumentReference) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH_RESULT)
            .add(nineBallMatch)
            .addOnSuccessListener { onSuccess(it) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun setNineBallMatch(
        documentReferenceId: String,
        nineBallMatch: NineBallMatch,
        mergeFields: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH_RESULT)
            .document(documentReferenceId)
            .set(nineBallMatch, SetOptions.mergeFields(mergeFields))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }

    }

    fun getNineBallMatch(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH_RESULT)
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun deleteNineBallMatch(
        documentReferenceId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(COLLECTION_NINE_BALL_MATCH_RESULT)
            .document(documentReferenceId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }

    }
}