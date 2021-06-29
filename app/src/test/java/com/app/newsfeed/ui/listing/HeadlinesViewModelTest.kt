package com.app.newsfeed.ui.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.newsfeed.FakeResponseUtility
import com.app.newsfeed.MainCoroutineRule
import com.app.newsfeed.data.DataRepository
import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.data.source.local.FakeLocalDataSource
import com.app.newsfeed.data.source.remote.FakeRemoteDataSource
import com.app.newsfeed.getOrAwaitValue
import com.app.newsfeed.pojo.EmptyView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HeadlinesViewModelTest {

    private lateinit var headlinesViewModel: HeadlinesViewModel

    private lateinit var dataRepository: DataRepository

    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource

    private lateinit var testCoroutineDispatcher : TestCoroutineDispatcher

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        fakeLocalDataSource = FakeLocalDataSource(mutableListOf())
        fakeRemoteDataSource = FakeRemoteDataSource()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource)

        testCoroutineDispatcher = TestCoroutineDispatcher()
        headlinesViewModel = HeadlinesViewModel(dataRepository, testCoroutineDispatcher)

    }

    @Test
    fun getArticles_successResponse_shouldDisplayList() = testCoroutineDispatcher.runBlockingTest {

        testCoroutineDispatcher.pauseDispatcher()

        headlinesViewModel.getArticles(1)

        val data1 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data1 , true)

        testCoroutineDispatcher.resumeDispatcher()

        val data2 = headlinesViewModel.articleList.getOrAwaitValue()
        Assert.assertEquals(data2 , ResultData.Success(FakeResponseUtility.getResponseWith2Article().articles))

        val data3 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)

    }

    @Test
    fun getArticles_errorResponse_shouldDisplayErrorView() = testCoroutineDispatcher.runBlockingTest {
        fakeRemoteDataSource.setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)
        testCoroutineDispatcher.pauseDispatcher()

        headlinesViewModel.getArticles(1)

        val data1 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data1 , true)

        testCoroutineDispatcher.resumeDispatcher()

        val data2 = headlinesViewModel.empty.getOrAwaitValue()
        Assert.assertEquals(data2 , EmptyView.NetworkError(FakeResponseUtility.getResponseWithError().message!!))

        val data3 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

    @Test
    fun getArticles_successResponse_shouldEmitLastPage() = testCoroutineDispatcher.runBlockingTest {
        fakeRemoteDataSource.setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_EMPTY_ARTICLE)
        testCoroutineDispatcher.pauseDispatcher()

        headlinesViewModel.getArticles(1)

        val data1 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data1 , true)

        testCoroutineDispatcher.resumeDispatcher()

        val data2 = headlinesViewModel.empty.getOrAwaitValue()
        Assert.assertEquals(data2 , EmptyView.LastPage(""))

        val data3 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

}