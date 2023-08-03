package com.example.cherry_pick_android.data.mapper

import com.example.cherry_pick_android.data.model.KeywordEntity

// 키워드 매핑 (domain 계층에 존재하는 키워드 데이터를 data 계층에서 사용하기 위한 것)
object KeywordMapper {
    fun mapperToKeywordEntity(keyword: String): KeywordEntity{
        return KeywordEntity(
            keyword = keyword
        )
    }
}