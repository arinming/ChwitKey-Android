package com.chwitkey.cherry_pick_android.data.remote.repository

import androidx.lifecycle.LiveData
import com.chwitkey.cherry_pick_android.data.db.KeywordDAO
import com.chwitkey.cherry_pick_android.data.mapper.KeywordMapper
import com.chwitkey.cherry_pick_android.data.model.KeywordEntity
import com.chwitkey.cherry_pick_android.domain.repository.KeywordRepository

// 키워드 Repository 구현체
class KeywordRepositoryImpl(
    private val dao: KeywordDAO
):KeywordRepository {
    override suspend fun deleteKeyword(keyword: String) {
        dao.deleteKeyword(keyword)
    }

    override fun readKeywordList(): LiveData<List<KeywordEntity>> {
        return dao.readKeywordList()
    }

    override suspend fun writeKeyword(keyword: String) {
        dao.writeKeyword(KeywordMapper.mapperToKeywordEntity(keyword))
    }

}