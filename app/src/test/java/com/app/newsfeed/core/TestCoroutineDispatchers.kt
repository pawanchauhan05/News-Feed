package com.app.newsfeed.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestCoroutineDispatchers : CoDispatcher {

    @ExperimentalCoroutinesApi
    override fun io(): CoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    override fun main(): CoroutineDispatcher = TestCoroutineDispatcher()
}