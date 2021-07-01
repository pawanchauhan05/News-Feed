package com.app.newsfeed.di

import com.app.newsfeed.data.DataRepository
import com.app.newsfeed.data.IDataRepository
import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.data.source.remote.IRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataRepositoryModule {

    @Singleton
    @Provides
    fun provideDataRepository(
        localDataSource: ILocalDataSource,
        remoteDataSource: IRemoteDataSource,
        ioDispatcher: CoroutineDispatcher
    ): IDataRepository {
        return DataRepository(
            localDataSource, remoteDataSource, ioDispatcher
        )
    }
}