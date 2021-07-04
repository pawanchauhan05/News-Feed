package com.app.newsfeed

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.app.newsfeed.data.DataRepository
import com.app.newsfeed.data.IDataRepository
import com.app.newsfeed.data.source.local.AppDatabase
import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.data.source.local.LocalDataSource
import com.app.newsfeed.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ServiceLocator {

    @Inject
    lateinit var localDataSource: LocalDataSource

    @Inject
    lateinit var remoteDataSource: IRemoteDataSource

    @Inject
    lateinit var dispatcher : CoroutineDispatcher

    private val lock = Any()
    private var database: AppDatabase? = null


    @Volatile
    var dataRepository: IDataRepository? = null
        @VisibleForTesting set

    fun provideDataRepository(context: Context): IDataRepository {
        synchronized(this) {
            return dataRepository ?: createDataRepository(context)
        }
    }

    private fun createDataRepository(context: Context): IDataRepository {
        val newRepo = DataRepository(createLocalDataSource(context), createRemoteDataSource(), dispatcher)
        dataRepository = newRepo
        return newRepo

        //return DataRepository(localDataSource, remoteDataSource)
    }


    private fun createRemoteDataSource(): IRemoteDataSource {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://newsapi.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(IRemoteDataSource::class.java)
    }

    private fun createLocalDataSource(context: Context): ILocalDataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.getArticleDao(), dispatcher)
    }

    private fun createDataBase(context: Context): AppDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "News-Feed.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                //TasksRemoteDataSource.deleteAllTasks()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            dataRepository = null
        }
    }
}