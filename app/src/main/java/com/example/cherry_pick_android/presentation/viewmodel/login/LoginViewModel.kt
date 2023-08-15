package com.example.cherry_pick_android.presentation.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.cherry_pick_android.domain.model.UserData
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private val _token = MutableLiveData<String>()
    private val _isInit = MutableLiveData<String>()
    companion object{
        const val TAG = "LoginViewModel"
    }

    val token: LiveData<String>
        get() = _token
    val isInit: LiveData<String>
        get() = _isInit

    fun updateSocialToken(Token: String){
        _token.value = Token
    }

    fun setIsinit(status: String){
        _isInit.value = status
    }

    fun getUserData(): LiveData<UserData>{
        return liveData {
            emit(userDataRepository.getUserData())
            Log.d(TAG, "UserData: ${userDataRepository.getUserData()}")
        }
    }

    fun setUserData(key: String, value: String){
        Log.d(TAG, "setUserData")
        viewModelScope.launch {
            userDataRepository.setUserData(key, value)
        }
    }

}