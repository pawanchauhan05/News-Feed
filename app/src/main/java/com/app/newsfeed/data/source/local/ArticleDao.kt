package com.app.newsfeed.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.newsfeed.pojo.Article
import io.reactivex.Completable

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article)

    @Query("SELECT * FROM article")
    fun getAllArticles(): List<Article>

    @Query("SELECT * FROM article WHERE page_number = :pageNumber")
    fun getArticlesAsPerPage(pageNumber : Int): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(articleList : List<Article>)

    @Query("DELETE FROM article")
    fun deleteAll()
}