package com.app.newsfeed.data

import androidx.lifecycle.LiveData
import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.pojo.Article
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface IDataRepository {

    suspend fun getHeadlines(queryParams: Map<String, String>, page: Int) : Flow<ResultData>
}