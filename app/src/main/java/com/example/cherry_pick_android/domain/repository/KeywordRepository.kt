package com.example.cherry_pick_android.domain.repository

import androidx.lifecycle.LiveData
import com.example.cherry_pick_android.data.model.KeywordEntity
import com.example.cherry_pick_android.data.data.Keyword
import javax.inject.Inject

interface KeywordRepository {
    suspend fun deleteKeyword(keyword: String)
    fun readKeywordList(): LiveData<List<KeywordEntity>>
    suspend fun writeKeyword(keyword: String)
}