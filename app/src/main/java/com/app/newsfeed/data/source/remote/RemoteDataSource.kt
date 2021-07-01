package com.app.newsfeed.data.source.remote

import com.app.newsfeed.pojo.Response

class RemoteDataSource(val remoteApi: RemoteApi) : IRemoteDataSource {

    override suspend fun getHeadlines(queryParams: Map<String, String>): Response {
        return remoteApi.getHeadlines(queryParams)
    }
}