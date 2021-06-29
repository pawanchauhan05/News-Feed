package com.app.newsfeed.pojo

sealed class EmptyView {
    data class EmptyData(val message : String) : EmptyView()
    data class NetworkError(val message : String) : EmptyView()
    data class LastPage(val message : String) : EmptyView()
}
