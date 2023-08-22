package com.chwitkey.cherry_pick_android.data.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchRecord(
    // 기본키에 nullable 옵션 -> 데이터 생성시 자동 설정됨
    @PrimaryKey val id: Int?,
    @ColumnInfo("record") val record: String
)
