package com.chwitkey.cherry_pick_android.presentation.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel: ViewModel() {
    private val _isDelete = MutableLiveData<String>()

    companion object{
        const val TAG = "MyPageViewModel"
    }
    val isDelete: LiveData<String>
        get() = _isDelete

    fun setDelete(value: String){
        _isDelete.value = value
        Log.d(TAG, "value:${value}")
    }

}
