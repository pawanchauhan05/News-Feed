package com.app.newsfeed.data.source.remote

import com.app.newsfeed.pojo.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RemoteApi {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(@QueryMap queryParams : Map<String, String>): Response

}