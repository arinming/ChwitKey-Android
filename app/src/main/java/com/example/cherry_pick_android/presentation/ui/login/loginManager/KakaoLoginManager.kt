package com.example.cherry_pick_android.presentation.ui.login.loginManager

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class KakaoLoginManager @Inject constructor(
    @ActivityContext private val context: Context
){
    companion object{
        const val TAG = "KakaoLoginManager"
    }
    private lateinit var kakaoLoginState: KakaoLoginState
    private lateinit var kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit

    // 실제 로그인 시작
    fun startKakaoLogin(updateSocialToken: (String) -> Unit){
        kakaoLoginState = getKakaoLoginState()
        kakaoLoginCallback = getLoginCallback(updateSocialToken)

        when(kakaoLoginState){
            KakaoLoginState.KAKAO_TALK_LOGIN -> onKakaoTalkLogin(updateSocialToken)
            KakaoLoginState.KAKAO_WEB_LOGIN -> onKakaoWebLogin()
        }
    }

    // 카카오톡 앱 설치 여부 확인
    private fun getKakaoLoginState(): KakaoLoginState =
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)){
            KakaoLoginState.KAKAO_TALK_LOGIN
        }else KakaoLoginState.KAKAO_WEB_LOGIN

    // 콜백함수
    private fun getLoginCallback(updateSocialToken: (String) -> Unit): (OAuthToken?, Throwable?) -> Unit{
        return{token, error->
            if(error != null){
                Log.d(TAG, error.message.toString())
            }else if(token != null){
                updateSocialToken(token.accessToken)
            }
        }
    }

    // 어플 로그인
    private fun onKakaoTalkLogin(updateSocialToken: (String) -> Unit){
        UserApiClient.instance.loginWithKakaoTalk(context){token, error ->
            if(error != null){
                Log.d(TAG, error.message.toString())
            }else if(token != null){
                updateSocialToken(token.accessToken)
            }
        }
    }

    // 웹 로그인
    private fun onKakaoWebLogin(){
        UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
    }
}

// 로그인 방식
enum class KakaoLoginState{
    KAKAO_TALK_LOGIN, KAKAO_WEB_LOGIN
}