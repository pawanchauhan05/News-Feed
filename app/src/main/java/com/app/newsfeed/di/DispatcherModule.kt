package com.app.newsfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {

    /*@Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DispatchersIO

    @Singleton
    @Provides
    @DispatchersIO
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO*/
}