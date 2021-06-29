package com.app.newsfeed

import android.app.Application
import com.app.newsfeed.data.IDataRepository

class AppController : Application() {

    val dataRepository: IDataRepository
        get() = ServiceLocator.provideDataRepository(this)

    override fun onCreate() {
        super.onCreate()
    }
}