package com.chwitkey.cherry_pick_android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 키워드 DB 생성
@Entity(tableName = "keyword_table")
data class KeywordEntity(@ColumnInfo(name = "keyword") var keyword: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var isSelected: Boolean = false

}