package com.app.newsfeed.data

import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.data.source.local.ILocalDataSource
import com.app.newsfeed.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataRepository(
    public val localDataSource: ILocalDataSource,
    public val remoteDataSource: IRemoteDataSource,
    public val coDispatcher: CoroutineDispatcher
) : IDataRepository {

    private val TAG = "DataRepository"

    override suspend fun getHeadlines(
        queryParams: Map<String, String>,
        page: Int
    ): Flow<ResultData> = flow {

        try {
            localDataSource.getArticlesAsPerPage(page).let {
                if (it.isNotEmpty()) {
                    emit(ResultData.Success(dataList = it) as ResultData)
                }
            }

            val data = remoteDataSource.getHeadlines(queryParams)
            data.articles.forEach {
                it.pageNumber = page
            }
            if(page == 1) {
                localDataSource.deleteAll()
            }
            localDataSource.insertArticle(data.articles)

            emit(ResultData.Success(dataList = localDataSource.getArticlesAsPerPage(pageNumber = page)) as ResultData)

        } catch (e: Exception) {
            emit(ResultData.Failure(e) as ResultData)
        }
    }


}