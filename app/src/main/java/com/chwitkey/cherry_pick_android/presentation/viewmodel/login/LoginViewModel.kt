package com.chwitkey.cherry_pick_android.presentation.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.chwitkey.cherry_pick_android.domain.model.UserData
import com.chwitkey.cherry_pick_android.domain.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private val _flag = MutableLiveData<String>()
    private val _isOutView = MutableLiveData<String>()
    companion object{
        const val TAG = "LoginViewModel"
    }


    val flag: LiveData<String>
        get() = _flag
    val isOutView: LiveData<String>
        get() = _isOutView

    fun getUserData(): LiveData<UserData>{
        return liveData {
            emit(userDataRepository.getUserData())
            Log.d(TAG, "UserData: ${userDataRepository.getUserData()}")
        }
    }

    fun setUserData(key: String, value: String){
        Log.d(TAG, "[setUserData] key:${key} value:${value}")
        viewModelScope.launch {
            userDataRepository.setUserData(key, value)
        }
    }

    fun setFlag(flag: String){
        _flag.value = flag
    }

    fun setIsOutView(value: String){
        _isOutView.value = value
    }


}