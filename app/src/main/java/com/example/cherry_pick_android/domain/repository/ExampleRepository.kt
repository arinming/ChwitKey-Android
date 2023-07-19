package com.example.cherry_pick_android.domain.repository

import com.example.cherry_pick_android.domain.model.ExampleResponse
import retrofit2.Response

interface ExampleRepository {
    suspend fun example(

    ): Response<ExampleResponse>
}