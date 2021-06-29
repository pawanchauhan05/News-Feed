package com.app.newsfeed.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.newsfeed.pojo.Article
import com.app.newsfeed.pojo.Source
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var articleDao: ArticleDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        articleDao = appDatabase.getArticleDao()
    }

    @Test
    fun insertAndReadArticle() = runBlocking {
        val article = Article(source = Source(101, "Anusha"), author = "Rahul",
            title = "Test2", description = "short Description", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:03:00Z", content = "Demonstrators hold signs that read in Portuguese")
        articleDao.insertArticle(article)

        val articleList = articleDao.getAllArticles()
        Assert.assertTrue(articleList.contains(article))
    }

    @Test
    fun insertAndReadArticleList() = runBlocking {
        val article1 = Article(source = Source(100, "Baisa"), author = "Pawan",
            title = "Test1", description = "short Description", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:01:00Z", content = "OAKLAND (CBS SF) — One person was dead")

        val article2 = Article(source = Source(101, "Anusha"), author = "Rahul",
            title = "Test2", description = "short Description", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:03:00Z", content = "Demonstrators hold signs that read in Portuguese")

        val listToInsert = listOf<Article>(article1, article2)

        articleDao.insertArticle(listToInsert)

        val articleList = articleDao.getAllArticles()

        Assert.assertEquals(articleList, listToInsert)
    }

    @Test
    fun insertAndReadArticleAsPerPage() = runBlocking {
        val article1 = Article(source = Source(100, "Baisa"), author = "Pawan",
            title = "Test1", description = "short Description Baisa", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:01:00Z", content = "OAKLAND (CBS SF) — One person was dead", pageNumber = 1)

        val article2 = Article(source = Source(101, "Anusha"), author = "Rahul",
            title = "Test2", description = "short Description Anusha", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:03:00Z", content = "Demonstrators hold signs that read in Portuguese", pageNumber = 1)

        val article3 = Article(source = Source(103, "Rajdeep"), author = "Vishvendra",
            title = "Test3", description = "short Description Rajdeep", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:06:00Z", content = "Demonstrators hold signs that read in Portuguese", pageNumber = 2)

        val listToInsert = listOf<Article>(article1, article2, article3)
        articleDao.insertArticle(listToInsert)

        val pageNumber = 2

        val articleList = articleDao.getArticlesAsPerPage(pageNumber)

        Assert.assertTrue(articleList.size == 1)

    }

    @Test
    fun insertAndDeleteArticle() = runBlocking {
        val article1 = Article(source = Source(100, "Baisa"), author = "Pawan",
            title = "Test1", description = "short Description", url = "no idea",
            urlToImage = "url to Image", publishedAt = "2021-06-20T05:01:00Z", content = "OAKLAND (CBS SF) — One person was dead")

        articleDao.insertArticle(article1)

        articleDao.deleteAll()
        val articleList = articleDao.getAllArticles()

        Assert.assertTrue(articleList.isEmpty())
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }
}