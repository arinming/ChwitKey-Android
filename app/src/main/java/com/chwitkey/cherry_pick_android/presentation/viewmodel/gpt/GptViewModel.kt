package com.chwitkey.cherry_pick_android.presentation.viewmodel.gpt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GptViewModel: ViewModel() {
    companion object{
        const val TAG = "GptViewModel"
    }

    private val _type = MutableLiveData<String>()

    val type: LiveData<String>
        get() = _type

    fun setType(type: String){
        Log.d(TAG, "SetTYPE")
        _type.value = type
    }
}