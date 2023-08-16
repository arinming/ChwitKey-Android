package com.example.cherry_pick_android.presentation.viewmodel.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cherry_pick_android.data.remote.response.article.ArticleCommendResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
) : ViewModel() {

    private val liveDataList = MutableLiveData<List<ArticleCommendResponse.Data>>()

    companion object {
        const val TAG = "ArticleViewModel"
    }
}