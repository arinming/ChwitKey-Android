package com.example.cherry_pick_android.presentation.ui.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.service.login.UserInfoService
import com.example.cherry_pick_android.databinding.ActivityLoginBinding
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.infrom.InformSettingActivity
import com.example.cherry_pick_android.presentation.ui.login.loginManager.KakaoLoginManager
import com.example.cherry_pick_android.presentation.ui.login.loginManager.NaverLoginManager
import com.example.cherry_pick_android.presentation.util.PlatformManager
import com.example.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var kakaoLoginManager: KakaoLoginManager
    @Inject
    lateinit var naverLoginManager: NaverLoginManager
    @Inject
    lateinit var userInfoService: UserInfoService

    private val viewModel: LoginViewModel by viewModels()

    companion object{
        const val TAG = "LoginActivity"
        private const val KAKAO = "kakao"
        private const val NAVER = "naver"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        // 기존 회원 여부 검사 (200: 통신성공, 404: 통신실패)
        viewModel.getUserData().observe(this@LoginActivity, Observer {
            if(it.userId != ""){
                lifecycleScope.launch {
                    val userInfoResponse = userInfoService.getUserInfo(it.userId)
                    val status = userInfoResponse.body()?.statusCode.toString()
                    withContext(Dispatchers.Main){
                        if(status == "200"){
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            viewModel.setIsinit("200")
                            startActivity(intent)
                            finish()
                        }else if(status == "404"){
                            val intent = Intent(this@LoginActivity, InformSettingActivity::class.java)
                            viewModel.setIsinit("404")
                            startActivity(intent)
                            finish()
                        }else{
                            Log.d(TAG, "ERROR")
                        }
                    }
                }
            }
        })

        // 텍스트 스타일 설정
        binding.tvExplain.text = textToBold(binding.tvExplain.text.toString(), 7, 17)
        binding.tvExplain2.text = textToBold(binding.tvExplain2.text.toString(), 0, 16)


        //onClickLogin()


    }


    // 특정 텍스트 부분 스타일 변경
    private fun textToBold(content: String, start: Int, end: Int): CharSequence{
        val builder = SpannableStringBuilder(content)
        builder.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    // 플랫폼에 따라 로그인 및 유저id 저장
    private fun onClickLogin(){
        with(binding){
            linearKakaoLoginBtn.setOnClickListener {
                PlatformManager.setPlatform(KAKAO)
                kakaoLoginManager.startKakaoLogin {
                    viewModel.updateSocialToken(it)
                    UserApiClient.instance.me { user, error ->
                        viewModel.setUserData("userId", user?.id.toString())
                    }
                }
            }

            linearNaverLoginBtn.setOnClickListener {
                PlatformManager.setPlatform(NAVER)
                naverLoginManager.startLogin {
                    viewModel.updateSocialToken(it)
                    NidOAuthLogin().callProfileApi(object: NidProfileCallback<NidProfileResponse>{
                        override fun onError(errorCode: Int, message: String) {}
                        override fun onFailure(httpStatus: Int, message: String) {}
                        override fun onSuccess(result: NidProfileResponse) {
                            viewModel.setUserData("userId", result.profile?.id.toString())
                        }
                    })
                }
            }
        }
    }


}