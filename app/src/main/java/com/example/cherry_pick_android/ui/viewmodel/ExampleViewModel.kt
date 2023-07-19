package com.example.cherry_pick_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cherry_pick_android.domain.model.ExampleResponse
import com.example.cherry_pick_android.domain.repository.ExampleRepository

class ExampleViewModel(
    private val exampleRepository: ExampleRepository
) : ViewModel() {

    // Api
    private val _example = MutableLiveData<ExampleResponse>()
    val example: LiveData<ExampleResponse> get() = _example
}