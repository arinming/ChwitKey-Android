package com.chwitkey.cherry_pick_android.domain.repository

import androidx.lifecycle.LiveData
import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity

interface RecordRepository {
    suspend fun addRecord(record: String)
    suspend fun deleteRecord(record: String)
    fun readRecordList(): LiveData<List<SearchRecordEntity>>
}