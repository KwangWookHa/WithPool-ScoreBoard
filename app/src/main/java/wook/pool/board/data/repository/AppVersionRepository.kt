package wook.pool.board.data.repository

import com.google.firebase.firestore.*
import wook.pool.board.base.Constant
import wook.pool.board.data.model.NineBallMatch
import wook.pool.board.data.model.Player
import javax.inject.Inject

class AppVersionRepository @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    fun getAppVersion(
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_APP_VERSION)
            .document(Constant.Document.DOCUMENT_SCORE_BOARD)
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

}