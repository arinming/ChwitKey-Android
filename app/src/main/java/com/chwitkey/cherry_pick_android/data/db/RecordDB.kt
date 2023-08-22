package com.chwitkey.cherry_pick_android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity


@Database(entities = [SearchRecordEntity::class], version = 1, exportSchema = false)
abstract class RecordDB: RoomDatabase() {
    abstract fun recordDAO(): RecordDAO
}