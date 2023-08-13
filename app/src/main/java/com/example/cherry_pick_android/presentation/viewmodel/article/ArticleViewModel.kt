package com.example.cherry_pick_android.presentation.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cherry_pick_android.data.remote.repository.ArticleRepository
import com.example.cherry_pick_android.data.remote.response.ArticleCommendResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    private val liveDataList = MutableLiveData<List<ArticleCommendResponse.Data>>()


    fun getLiveDataObserver(): MutableLiveData<List<ArticleCommendResponse.Data>> {
        return liveDataList
    }

    suspend fun loadListOfData() {
        repository.makeApiCall(liveDataList)
    }
}