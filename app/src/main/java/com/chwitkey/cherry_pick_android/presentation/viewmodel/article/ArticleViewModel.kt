package com.chwitkey.cherry_pick_android.presentation.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chwitkey.cherry_pick_android.data.data.ArticleItem
import com.chwitkey.cherry_pick_android.data.remote.response.article.ArticleCommendResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
) : ViewModel() {

    private val articleList = MutableLiveData<List<ArticleCommendResponse.Data>>()
    private val _newList = MutableLiveData<MutableList<ArticleItem>>()

    val newList: LiveData<MutableList<ArticleItem>>
        get() = _newList

    companion object {
        const val TAG = "ArticleViewModel"
    }

    fun setNewList(list: MutableList<ArticleItem>){
        _newList.value = list
    }

    fun initList() {

    }

    fun nextList() {
    }
}