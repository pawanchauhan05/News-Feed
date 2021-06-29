package com.app.newsfeed.data.source

import com.app.newsfeed.FakeResponseUtility
import com.app.newsfeed.MainCoroutineRule
import com.app.newsfeed.data.DataRepository
import com.app.newsfeed.data.source.local.FakeLocalDataSource
import com.app.newsfeed.data.source.remote.FakeRemoteDataSource
import com.app.newsfeed.pojo.Article
import com.app.newsfeed.pojo.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.net.SocketTimeoutException

class DataRepositoryTest {
    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource

    private lateinit var dataRepository: DataRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        fakeLocalDataSource = FakeLocalDataSource(mutableListOf())
        fakeRemoteDataSource = FakeRemoteDataSource()

        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, Dispatchers.Main)
    }

    @Test
    fun getHeadlines_requestAllHeadlinesFromRemoteSource() = mainCoroutineRule.runBlockingTest {
        val response = fakeRemoteDataSource.getHeadlines(emptyMap())
        Assert.assertThat(response.articles, IsEqual(FakeResponseUtility.getResponseWith2Article().articles))
    }

    @Test
    fun getHeadlines_pageOne_successRemoteResponse() = mainCoroutineRule.runBlockingTest {

        val list = dataRepository.getHeadlines(emptyMap(), 1).toList()
        Assert.assertEquals(
            list, listOf(
                ResultData.Success(FakeResponseUtility.getResponseWith2Article().articles)
            )
        )
        Assert.assertEquals(
            dataRepository.localDataSource.getAllArticles(),
            FakeResponseUtility.getResponseWith2Article().articles
        )
    }

    @Test
    fun getHeadlines_pageOne_errorRemoteResponse() = mainCoroutineRule.runBlockingTest {
        fakeRemoteDataSource.setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        val list = dataRepository.getHeadlines(emptyMap(), 1).toList()

        val expectedList = mutableListOf<ResultData>().apply {
            add(ResultData.Failure(FakeResponseUtility.getResponseWithError()))
        }

        Assert.assertEquals(list, expectedList)

    }

    @Test
    fun getHeadlines_pageOne_successLocalResponse_successRemoteResponse() = mainCoroutineRule.runBlockingTest {
        fakeLocalDataSource.insertFakeArticles(FakeResponseUtility.getResponseWith2Article().articles)
        fakeLocalDataSource.insertFakeArticles(FakeResponseUtility.getResponseForPage2().articles)


        val list = dataRepository.getHeadlines(emptyMap(), 1).toList()

        val expectedList = mutableListOf<ResultData>().apply {
            add(ResultData.Success(FakeResponseUtility.getResponseWith2Article().articles))
            add(ResultData.Success(FakeResponseUtility.getResponseWith2Article().articles))
        }

        Assert.assertEquals(list, expectedList)

        Assert.assertEquals(dataRepository.localDataSource.getAllArticles(), FakeResponseUtility.getResponseWith2Article().articles)
    }

    @Test
    fun getHeadlines_pageTwo_successLocalResponse_errorRemoteResponse() = mainCoroutineRule.runBlockingTest {
        fakeLocalDataSource.insertFakeArticles(FakeResponseUtility.getResponseWith2Article().articles)
        fakeLocalDataSource.insertFakeArticles(FakeResponseUtility.getResponseForPage2().articles)
        fakeRemoteDataSource.setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        val list = dataRepository.getHeadlines(emptyMap(), 2).toList()

        val expectedList = mutableListOf<ResultData>().apply {
            add(ResultData.Success(FakeResponseUtility.getResponseForPage2().articles))
            add(ResultData.Failure(FakeResponseUtility.getResponseWithError()))
        }

        Assert.assertEquals(list, expectedList)

        val localSourceList = mutableListOf<Article>().apply {
            addAll(FakeResponseUtility.getResponseWith2Article().articles)
            addAll(FakeResponseUtility.getResponseForPage2().articles)
        }

        Assert.assertEquals(dataRepository.localDataSource.getAllArticles(), localSourceList)
    }
}