package wook.pool.board.data.repository

import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import wook.pool.board.Constant
import wook.pool.board.data.model.AppVersion
import javax.inject.Inject

class AppVersionRepository @Inject constructor(
        private val fireStore: FirebaseFirestore
) {

    suspend fun getAppVersion() = withContext(Dispatchers.IO) {
        fireStore.collection(Constant.Collection.COLLECTION_APP_VERSION)
                .document(Constant.Document.DOCUMENT_SCORE_BOARD)
                .get()
                .await()
                .toObject(AppVersion::class.java)
    }

}