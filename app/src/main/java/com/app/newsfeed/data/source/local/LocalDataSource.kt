package com.app.newsfeed.data.source.local

import com.app.newsfeed.pojo.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LocalDataSource internal constructor(
    private val articleDao: ArticleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ILocalDataSource {


    override fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    override fun insertArticle(articles: List<Article>) {
        articleDao.insertArticle(articles)
    }

    override fun getAllArticles(): List<Article> {
        return articleDao.getAllArticles()
    }

    override fun getArticlesAsPerPage(pageNumber: Int): List<Article> {
        return articleDao.getArticlesAsPerPage(pageNumber)
    }

    override fun deleteAll() {
        articleDao.deleteAll()
    }

}