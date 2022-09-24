package wook.pool.board.data.source.remote.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import wook.pool.board.Constant
import wook.pool.board.data.model.NineBallMatch
import javax.inject.Inject


class MatchRepository @Inject constructor(
        private val fireStore: FirebaseFirestore
) {

    suspend fun addNineBallMatch(nineBallMatch: NineBallMatch): DocumentReference =
            fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH).add(nineBallMatch).await()

    suspend fun updateNineBallMatch(
            documentPath: String,
            data: Map<String, Any>,
    ) {
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
                .document(documentPath)
                .update(data)
                .await()
    }

    suspend fun deleteNineBallMatch(documentPath: String) {
        fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
                .document(documentPath)
                .delete()
                .await()
    }

    suspend fun getHeadToHeadRecords(
            playerLeftName: String,
            playerRightName: String): List<NineBallMatch> {
        val oneMonthAgo = DateTime.now().withTimeAtStartOfDay().minusMonths(1).toDate()
        return fireStore.collection(Constant.Collection.COLLECTION_NINE_BALL_MATCH)
                .whereEqualTo(Constant.Field.FILED_PLAYER_LEFT_NAME, playerLeftName)
                .whereEqualTo(Constant.Field.FILED_PLAYER_RIGHT_NAME, playerRightName)
                .whereGreaterThanOrEqualTo(Constant.Field.FILED_END_TIME_STAMP, Timestamp(oneMonthAgo))
                .orderBy(Constant.Field.FILED_END_TIME_STAMP, Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(NineBallMatch::class.java)
    }
}