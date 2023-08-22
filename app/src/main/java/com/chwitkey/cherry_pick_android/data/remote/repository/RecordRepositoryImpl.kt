package com.chwitkey.cherry_pick_android.data.remote.repository

import androidx.lifecycle.LiveData
import com.chwitkey.cherry_pick_android.data.db.RecordDAO
import com.chwitkey.cherry_pick_android.data.mapper.RecordMapper
import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity
import com.chwitkey.cherry_pick_android.domain.repository.RecordRepository

class RecordRepositoryImpl(
    private val dao: RecordDAO
): RecordRepository {
    override suspend fun deleteRecord(record: String) {
        dao.deleteRecord(record)
    }

    override fun readRecordList(): LiveData<List<SearchRecordEntity>> {
        return dao.readRecordList()
    }

    override suspend fun addRecord(record: String) {
        dao.addRecord(RecordMapper.mapperToRecordEntity(record))
    }
}