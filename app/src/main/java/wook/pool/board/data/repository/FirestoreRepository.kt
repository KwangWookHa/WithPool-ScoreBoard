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
        private const val nineBallMatchResult = "nine_ball_match_result"
    }

    fun insertPlayer(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("players")
            .add(player)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getPlayers(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        db.collection("players")
            .get()
            .addOnSuccessListener { onSuccess(it) }
            .addOnFailureListener { onFailure(it) }
    }

    fun addNineBallMatch(
        nineBallMatch: NineBallMatch,
        onSuccess: (DocumentReference) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(nineBallMatchResult)
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
        db.collection(nineBallMatchResult)
            .document(documentReferenceId)
            .set(nineBallMatch, SetOptions.mergeFields(mergeFields))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }

    }
}