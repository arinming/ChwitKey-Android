package com.example.cherry_pick_android.presentation.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.cherry_pick_android.domain.model.UserId
import com.example.cherry_pick_android.domain.repository.UserIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userIdRepository: UserIdRepository
): ViewModel() {
    private val _token = MutableLiveData<String>()
    companion object{
        const val TAG = "LoginViewModel"
    }

    val token: LiveData<String>
        get() = _token
    fun updateSocialToken(Token: String){
        _token.value = Token
    }

    fun getUserId(): LiveData<UserId>{
        Log.d(TAG, "getUserId")
        return liveData {
            emit(userIdRepository.getUserId())
        }
    }

    fun setUserId(key: String, value: String){
        Log.d(TAG, "setUserId")
        viewModelScope.launch {
            userIdRepository.setUserId(key, value)
        }
    }

}