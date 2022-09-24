package wook.pool.board.data.repository

import com.google.firebase.firestore.*
import wook.pool.board.Constant
import javax.inject.Inject

class CountRepository @Inject constructor(
        private val fireStore: FirebaseFirestore
) {

    fun getNineBallMatchTotalCount(
            documentPath: String,
            onSuccess: (DocumentSnapshot) -> Unit,
            onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_COUNT)
                .document(documentPath)
                .get()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure)
    }

    fun updateNineBallMatchTotalCount(
            documentPath: String,
            count: Long,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_COUNT)
                .document(documentPath)
                .update(Constant.Collection.COLLECTION_COUNT, count)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener(onFailure)
    }
}