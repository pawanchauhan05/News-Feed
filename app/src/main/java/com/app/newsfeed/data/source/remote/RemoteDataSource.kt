package com.app.newsfeed.data.source.remote

import androidx.lifecycle.LiveData
import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.pojo.Article
import com.app.newsfeed.pojo.Response
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RemoteDataSource {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(@QueryMap queryParams : Map<String, String>): Response
}