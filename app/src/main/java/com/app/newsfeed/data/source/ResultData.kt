package com.app.newsfeed.data.source

import com.app.newsfeed.pojo.Article

sealed class ResultData {
    data class Success(val dataList : List<Article>) : ResultData()
    data class Failure(val exception : Exception) : ResultData()
    data class Progress(val isShow : Boolean) : ResultData()
}