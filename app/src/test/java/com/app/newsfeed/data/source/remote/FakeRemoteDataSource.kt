package com.app.newsfeed.data.source.remote

import com.app.newsfeed.FakeResponseUtility
import com.app.newsfeed.pojo.Response

class FakeRemoteDataSource :  IRemoteDataSource {

    enum class Data {
        SHOULD_RETURN_ERROR,
        SHOULD_RETURN_2_ARTICLE,
        SHOULD_RETURN_EMPTY_ARTICLE
    }

    private var status = Data.SHOULD_RETURN_2_ARTICLE

    fun setStatus(value: Data) {
        status = value
    }

    override suspend fun getHeadlines(queryParams: Map<String, String>): Response {
        return when(status) {
            Data.SHOULD_RETURN_ERROR -> throw FakeResponseUtility.getResponseWithError()
            Data.SHOULD_RETURN_2_ARTICLE -> FakeResponseUtility.getResponseWith2Article()
            Data.SHOULD_RETURN_EMPTY_ARTICLE -> FakeResponseUtility.getResponseWithEmptyArticle()
        }
    }
}