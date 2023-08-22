package com.chwitkey.cherry_pick_android.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity

@Dao
interface RecordDAO {
    @Query("SELECT * FROM search_record_table")
    fun readRecordList(): LiveData<List<SearchRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecord(recordEntity: SearchRecordEntity)

    @Query("DELETE FROM search_record_table WHERE record = :record")
    suspend fun deleteRecord(record: String)
}