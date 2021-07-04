package com.app.newsfeed.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.newsfeed.FakeResponseUtility
import com.app.newsfeed.MainCoroutineRule
import com.app.newsfeed.data.DataRepository
import com.app.newsfeed.data.IDataRepository
import com.app.newsfeed.data.source.local.FakeLocalDataSource
import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.data.source.remote.FakeRemoteDataSource
import com.app.newsfeed.data.source.remote.IRemoteDataSource
import com.app.newsfeed.pojo.Article
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class DataRepositoryHiltTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Inject
    lateinit var fakeLocalDataSource: ILocalDataSource

    @Inject
    lateinit var fakeRemoteDataSource: IRemoteDataSource

    @Inject
    lateinit var coDispatcher: CoroutineDispatcher

    private lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, coDispatcher)
    }

    @Test
    fun getHeadlines_requestAllHeadlinesFromRemoteSource() = mainCoroutineRule.runBlockingTest {
        val response = dataRepository.remoteDataSource.getHeadlines(emptyMap())
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
        (fakeRemoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        val list = dataRepository.getHeadlines(emptyMap(), 1).toList()

        val expectedList = mutableListOf<ResultData>().apply {
            add(ResultData.Failure(FakeResponseUtility.getResponseWithError()))
        }

        Assert.assertEquals(list, expectedList)

    }

    @Test
    fun getHeadlines_pageOne_successLocalResponse_successRemoteResponse() = mainCoroutineRule.runBlockingTest {
        (fakeLocalDataSource as FakeLocalDataSource).insertFakeArticles(FakeResponseUtility.getResponseWith2Article().articles)
        (fakeLocalDataSource as FakeLocalDataSource).insertFakeArticles(FakeResponseUtility.getResponseForPage2().articles)


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
        (fakeLocalDataSource as FakeLocalDataSource).insertFakeArticles(FakeResponseUtility.getResponseWith2Article().articles)
        (fakeLocalDataSource as FakeLocalDataSource).insertFakeArticles(FakeResponseUtility.getResponseForPage2().articles)
        (fakeRemoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

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

