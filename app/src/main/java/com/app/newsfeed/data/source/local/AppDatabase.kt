package com.app.newsfeed.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.newsfeed.pojo.Article
import com.app.newsfeed.pojo.Source

@Database(entities = [Article::class, Source::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao
}