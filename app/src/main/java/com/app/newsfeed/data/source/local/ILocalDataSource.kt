package com.app.newsfeed.data.source.local

import com.app.newsfeed.pojo.Article

interface ILocalDataSource {

    fun insertArticle(article : Article)

    fun insertArticle(articles: List<Article>)

    fun getAllArticles() : List<Article>

    fun getArticlesAsPerPage(pageNumber : Int) : List<Article>

    fun deleteAll()
}