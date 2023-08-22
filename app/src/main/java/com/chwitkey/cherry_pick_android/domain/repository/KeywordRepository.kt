package com.chwitkey.cherry_pick_android.domain.repository

import androidx.lifecycle.LiveData
import com.chwitkey.cherry_pick_android.data.model.KeywordEntity

interface KeywordRepository {
    suspend fun deleteKeyword(keyword: String)
    fun readKeywordList(): LiveData<List<KeywordEntity>>
    suspend fun writeKeyword(keyword: String)
}