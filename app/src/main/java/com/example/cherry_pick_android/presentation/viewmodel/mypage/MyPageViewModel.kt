package com.example.cherry_pick_android.presentation.viewmodel.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel: ViewModel() {
    private val _isDelete = MutableLiveData<String>()

    val isDelete: LiveData<String>
        get() = _isDelete

    fun setDelete(value: String){
        _isDelete.value = value
    }
}
