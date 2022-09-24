package wook.pool.board.data.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class AppVersion(

        @DocumentId
        val documentId: String? = null,

        val versionName: String? = null,

        @JvmField
        val isImmediateUpdate: Boolean? = null,

        ) : Serializable