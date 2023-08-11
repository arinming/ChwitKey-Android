package com.example.cherry_pick_android.domain.usecase

import com.example.cherry_pick_android.domain.repository.RecordRepository
import javax.inject.Inject

class AddRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(record: String) = recordRepository.addRecord(record)
}