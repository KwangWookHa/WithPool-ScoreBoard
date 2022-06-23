package wook.pool.board.data.repository

import com.google.firebase.firestore.*
import wook.pool.board.base.Constant
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    fun addNineBallMatch(
        nineBallMatch: NineBallMatch,
        onSuccess: (DocumentReference) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
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
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
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
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
            .document(documentPath)
            .set(nineBallMatch, SetOptions.mergeFields(mergeFields))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)

    }

    fun getNineBallMatch(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun deleteNineBallMatch(
        documentPath: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
            .document(documentPath)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)

    }
}