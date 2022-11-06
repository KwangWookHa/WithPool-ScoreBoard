package wook.pool.board.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wook.pool.board.data.source.remote.repository.AppVersionRepository
import wook.pool.board.data.source.remote.repository.MatchRepository
import wook.pool.board.data.source.remote.repository.PlayerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideMatchRepository(fireStore: FirebaseFirestore): MatchRepository = MatchRepository(fireStore)

    @Singleton
    @Provides
    fun provideAppVersionRepository(fireStore: FirebaseFirestore): AppVersionRepository = AppVersionRepository(fireStore)

    @Singleton
    @Provides
    fun providePlayerRepository(fireStore: FirebaseFirestore): PlayerRepository = PlayerRepository(fireStore)


}