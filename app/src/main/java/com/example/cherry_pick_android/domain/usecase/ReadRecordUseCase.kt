package com.example.cherry_pick_android.domain.usecase

import androidx.lifecycle.LiveData
import com.example.cherry_pick_android.data.model.SearchRecordEntity
import com.example.cherry_pick_android.domain.repository.RecordRepository
import javax.inject.Inject

class ReadRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    operator fun invoke(): LiveData<List<SearchRecordEntity>> = recordRepository.readRecordList()
}