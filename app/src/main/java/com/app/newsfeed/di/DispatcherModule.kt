package com.app.newsfeed.di

import com.app.newsfeed.core.CoDispatcher
import com.app.newsfeed.core.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    fun providesIoDispatcher(): CoDispatcher = CoroutineDispatchers()

    /*@Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main*/
}