package com.chwitkey.cherry_pick_android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_record_table")
data class SearchRecordEntity(@ColumnInfo(name = "record") var record: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
