package com.chwitkey.cherry_pick_android.data.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Article(
    @PrimaryKey @ColumnInfo(name = "id") val articleId: String,
    val title: String,
    val company: String,
    val time: String,
)