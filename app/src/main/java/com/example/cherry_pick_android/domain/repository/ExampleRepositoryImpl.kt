package com.example.cherry_pick_android.domain.repository

import com.example.cherry_pick_android.domain.model.ExampleResponse
import retrofit2.Response

class ExampleRepositoryImpl : ExampleRepository {
    override suspend fun example(

    ): Response<ExampleResponse> {
        TODO("Not yet implemented")
    }
}