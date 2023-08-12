package com.example.cherry_pick_android.presentation.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cherry_pick_android.data.remote.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeNewsViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {
    var liveData = MutableLiveData<String>("응답 없음")

    fun getData() = liveData

    init {

    }
/*
    private fun loadData() {
        viewModelScope.launch {
            val data = repository.checkArticle("param1", "param2", Pageable)
            when (data.isCanceled) {
                true -> {
                    liveData.postValue(data.toString())
                }
                else -> {
                }
            }
        }
    }

 */
}