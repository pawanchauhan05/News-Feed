package com.app.newsfeed

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppController : Application() {

    /*@Inject
    lateinit var localDataSource: LocalDataSource

    @Inject
    lateinit var remoteDataSource: RemoteDataSource*/

    /*val dataRepository: IDataRepository
        get() = ServiceLocator.provideDataRepository(this)*/

    //val dataRepository : IDataRepository = DataRepository(localDataSource, remoteDataSource)

    override fun onCreate() {
        super.onCreate()
    }
}