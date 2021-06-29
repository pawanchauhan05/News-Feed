package com.app.newsfeed.data.source.local

import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.pojo.Article

class FakeLocalDataSource(private var articlesList: MutableList<Article> = mutableListOf()) : ILocalDataSource {

    fun insertFakeArticles(articles: List<Article>) {
        articlesList.addAll(articles)
    }

    override fun insertArticle(article: Article) {
        articlesList.add(article)
    }

    override fun insertArticle(articles: List<Article>) {
        articlesList.addAll(articles)
    }

    override fun getAllArticles(): List<Article> {
        return articlesList
    }

    override fun getArticlesAsPerPage(pageNumber: Int): List<Article> {
        return articlesList.filter {
            it.pageNumber == pageNumber
        }
    }

    override fun deleteAll() {
        articlesList.clear()
    }
}