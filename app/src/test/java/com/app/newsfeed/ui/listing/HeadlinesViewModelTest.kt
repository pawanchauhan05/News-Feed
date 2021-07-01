package com.app.newsfeed.ui.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.newsfeed.FakeResponseUtility
import com.app.newsfeed.MainCoroutineRule
import com.app.newsfeed.core.CoDispatcher
import com.app.newsfeed.core.TestCoroutineDispatchers
import com.app.newsfeed.data.DataRepository
import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.data.source.local.FakeLocalDataSource
import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.data.source.remote.FakeRemoteDataSource
import com.app.newsfeed.data.source.remote.IRemoteDataSource
import com.app.newsfeed.getOrAwaitValue
import com.app.newsfeed.pojo.EmptyView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
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
@ExperimentalCoroutinesApi
class HeadlinesViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fakeLocalDataSource: ILocalDataSource

    @Inject
    lateinit var fakeRemoteDataSource: IRemoteDataSource

    @Inject
    lateinit var coDispatcher: CoDispatcher

    private lateinit var dataRepository: DataRepository

    private lateinit var headlinesViewModel: HeadlinesViewModel


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
        hiltRule.inject()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, coDispatcher)
        //testCoroutineDispatcher = TestCoroutineDispatcher()
        headlinesViewModel = HeadlinesViewModel(dataRepository, coDispatcher)

        if(coDispatcher.io() is TestCoroutineDispatcher) {
            System.out.println("RIGHT " + coDispatcher.io().toString())
        } else {
            System.out.println("WRONG")
        }
    }

    @Test
    fun getArticles_successResponse_shouldDisplayList() = (coDispatcher.io() as TestCoroutineDispatcher).runBlockingTest {
        (coDispatcher.io() as TestCoroutineDispatcher).pauseDispatcher()

        headlinesViewModel.getArticles(1)

        /*val data1 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data1 , true)

        (coDispatcher.io() as TestCoroutineDispatcher).resumeDispatcher()

        val data2 = headlinesViewModel.articleList.getOrAwaitValue()
        Assert.assertEquals(data2 , ResultData.Success(FakeResponseUtility.getResponseWith2Article().articles))

        val data3 = headlinesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)*/

    }

    /*@Test
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
    }*/



}
