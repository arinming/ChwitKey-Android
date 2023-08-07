package com.example.cherry_pick_android.data.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Article(
    @PrimaryKey @ColumnInfo(name = "id") val articleId: String,
    val title: String
)