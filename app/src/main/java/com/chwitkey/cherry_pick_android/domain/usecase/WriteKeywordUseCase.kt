package com.chwitkey.cherry_pick_android.domain.usecase

import com.chwitkey.cherry_pick_android.domain.repository.KeywordRepository
import javax.inject.Inject

class WriteKeywordUseCase @Inject constructor(
    private val keywordRepository: KeywordRepository
) {
    suspend operator fun invoke(keyword: String)
    = keywordRepository.writeKeyword(keyword)
}