package wook.pool.board.data.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Player(

        @DocumentId
        val documentId: String? = null,

        val club: String? = null,

        val name: String? = null,

        val handicap: Int? = null,

        ) : Serializable