package com.example.cherry_pick_android.presentation.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cherry_pick_android.data.remote.repository.ArticleRepository
import com.example.cherry_pick_android.data.remote.response.ArticleCommendResponse
import kotlinx.coroutines.launch

class ArticleViewModel: ViewModel() {
    private val repository = ArticleRepository()

    private val _result = MutableLiveData<ArticleCommendResponse>()
    val result: LiveData<ArticleCommendResponse>
        get() = _result

    fun getAllArticle() = viewModelScope.launch {
        _result.value = repository.getAllArticle()
    }
}