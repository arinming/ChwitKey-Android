package com.example.cherry_pick_android.presentation.viewmodel.login

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
    companion object{
        const val TAG = "LoginViewModel"
    }

    val token: LiveData<String>
        get() = _token
    fun updateSocialToken(Token: String){
        _token.value = Token
    }

    fun getUserData(): LiveData<UserData>{
        return liveData {
            emit(userDataRepository.getUserData())
        }
    }

    fun setUserData(key: String, value: String){
        viewModelScope.launch {
            userDataRepository.setUserData(key, value)
        }
    }

}