package com.app.newsfeed.di

import android.content.Context
import androidx.room.Room
import com.app.newsfeed.core.CoDispatcher
import com.app.newsfeed.data.source.local.AppDatabase
import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.data.source.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDataSourceModule {

    /*@Inject
    lateinit var coDispatcher: CoDispatcher*/

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "News-Feed.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(appDatabase: AppDatabase, coDispatcher: CoDispatcher) : ILocalDataSource {
        return LocalDataSource(appDatabase.getArticleDao(), coDispatcher)
    }

    /*@Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO*/

}