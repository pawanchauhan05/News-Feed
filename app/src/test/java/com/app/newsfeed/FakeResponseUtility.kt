package com.app.newsfeed

import com.app.newsfeed.pojo.Article
import com.app.newsfeed.pojo.Response
import com.app.newsfeed.pojo.Source
import java.net.SocketTimeoutException

object FakeResponseUtility {

    private val ex : Exception = SocketTimeoutException("TIMEOUT ERROR!")
    private val emptyList = emptyList<Article>()

    fun getResponseWith2Article() : Response {

        val article1 = Article(source = Source(100, "Baisa"), author = "Pawan",
            title = "Test1", description = "short Description Baisa", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:01:00Z", content = "OAKLAND (CBS SF) — One person was dead", pageNumber = 1)

        val article2 = Article(source = Source(101, "Anusha"), author = "Rahul",
            title = "Test2", description = "short Description Anusha", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:03:00Z", content = "Demonstrators hold signs that read in Portuguese", pageNumber = 1)

        val article3 = Article(source = Source(103, "Rajdeep"), author = "Vishvendra",
            title = "Test3", description = "short Description Rajdeep", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:06:00Z", content = "Demonstrators hold signs that read in Portuguese", pageNumber = 2)

        return Response("Success", listOf(article1, article2), "Success")

    }

    fun getResponseForPage2() : Response {
        val article = Article(source = Source(103, "Rajdeep"), author = "Vishvendra",
            title = "Test3", description = "short Description Rajdeep", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:06:00Z", content = "Demonstrators hold signs that read in Portuguese", pageNumber = 2)

        return Response("Success", listOf(article), "Success")
    }

    fun getResponseWithError() : Exception {
        return ex
    }

    fun getResponseWithEmptyArticle() : Response {
        return Response("Success", emptyList, "Success")
    }
}