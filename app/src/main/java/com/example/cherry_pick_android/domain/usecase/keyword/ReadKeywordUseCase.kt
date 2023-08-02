package com.example.cherry_pick_android.domain.usecase.keyword

import androidx.lifecycle.LiveData
import com.example.cherry_pick_android.data.model.KeywordEntity
import com.example.cherry_pick_android.domain.repository.KeywordRepository
import javax.inject.Inject

class ReadKeywordUseCase @Inject constructor(
    private val keywordRepository: KeywordRepository
){
    operator fun invoke(): LiveData<List<KeywordEntity>>
    = keywordRepository.readKeywordList()
}