package wook.pool.board.data.repository

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import wook.pool.board.base.Constant
import wook.pool.board.data.model.NineBallMatch
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