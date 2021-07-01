package com.app.newsfeed.di

import com.app.newsfeed.core.CoDispatcher
import com.app.newsfeed.core.TestCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatcherModule::class]
)
class FakeDispatcherModule {

    @Singleton
    @Provides
    fun providesIoDispatcher() : CoDispatcher = TestCoroutineDispatchers()
}