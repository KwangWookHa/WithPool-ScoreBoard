package wook.pool.board.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wook.pool.board.data.source.remote.repository.MatchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {


    @Singleton
    @Provides
    fun provideFirestoreRepository(fireStore: FirebaseFirestore): MatchRepository = MatchRepository(fireStore)


    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore


}