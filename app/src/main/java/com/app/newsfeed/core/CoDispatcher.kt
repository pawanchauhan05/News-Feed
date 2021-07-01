package com.app.newsfeed.core

import kotlinx.coroutines.CoroutineDispatcher

interface CoDispatcher {

    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}