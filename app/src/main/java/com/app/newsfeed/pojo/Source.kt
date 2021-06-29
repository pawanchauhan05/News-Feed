package com.app.newsfeed.pojo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Source(
    @PrimaryKey(autoGenerate = true) val uniqueId: Int = 1,
    @ColumnInfo(name = "name") val name: String? = ""
) : Parcelable