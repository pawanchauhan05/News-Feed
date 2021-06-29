package com.app.newsfeed.pojo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Article(
    @Embedded(prefix = "article_") val source: Source,
    @ColumnInfo(name = "author") val author: String? = "",
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "description") val description: String? = "",
    @ColumnInfo(name = "url") val url: String? = "",
    @ColumnInfo(name = "urlToImage") val urlToImage: String? = "",
    @PrimaryKey @ColumnInfo(name = "publishedAt") val publishedAt: String = "",
    @ColumnInfo(name = "content") val content: String? = "",
    @ColumnInfo(name = "page_number") var pageNumber : Int = 0
) : Parcelable
