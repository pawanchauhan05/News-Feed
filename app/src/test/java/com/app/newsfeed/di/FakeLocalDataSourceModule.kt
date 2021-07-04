package com.app.newsfeed.di

import com.app.newsfeed.data.source.local.FakeLocalDataSource
import com.app.newsfeed.data.source.local.ILocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDataSourceModule::class]
)
object FakeLocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(): ILocalDataSource {
        return FakeLocalDataSource()
    }

    /*@Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO*/
}
