package com.example.cherry_pick_android.data.remote.request

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cherry_pick_android.data.model.KeywordEntity

// 키워드 DB
@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class KeywordDB: RoomDatabase() {
    abstract fun keywordDAO(): KeywordDAO
}