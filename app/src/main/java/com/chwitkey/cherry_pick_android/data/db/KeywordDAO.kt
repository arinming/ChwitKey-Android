package com.chwitkey.cherry_pick_android.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chwitkey.cherry_pick_android.data.model.KeywordEntity

// 키워드 DAO
@Dao
interface KeywordDAO {
    // 테이블 불러오기
    @Query("SELECT * FROM keyword_table")
    fun readKeywordList(): LiveData<List<KeywordEntity>>

    // 키워드 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun writeKeyword(keywordEntity: KeywordEntity)

    // 키워드 삭제
    @Query("DELETE FROM keyword_table WHERE keyword = :keyword")
    suspend fun deleteKeyword(keyword: String)
}