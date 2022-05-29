package wook.pool.board.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import wook.pool.board.data.model.NineBallMatchResult
import wook.pool.board.data.model.Player
import javax.inject.Inject

class FirestoreRepository @Inject constructor() {

    private val db: FirebaseFirestore = Firebase.firestore

    fun insertPlayer(player: Player, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        player.name?.let {
            db.collection("players")
                .document(it)
                .set(player)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun getPlayers(onSuccess: (QuerySnapshot) -> Unit, onFailure: (e: Exception) -> Unit) {
        db.collection("players")
            .get()
            .addOnSuccessListener { onSuccess(it) }
            .addOnFailureListener { onFailure(it) }
    }

    fun insertNineBallMatchResult(
        nineBallMatchResult: NineBallMatchResult,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val documentName =
            with(nineBallMatchResult) { "${playerLeftName}vs${playerRightName}_${matchStartDateTime}" }
        db.collection("nine_ball_match_result")
            .document(documentName)
            .set(nineBallMatchResult)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}