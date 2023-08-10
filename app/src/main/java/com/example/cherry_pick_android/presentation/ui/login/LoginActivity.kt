package com.example.cherry_pick_android.presentation.ui.login

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.service.SignUpService
import com.example.cherry_pick_android.databinding.ActivityLoginBinding
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.infrom.InformSettingActivity
import com.example.cherry_pick_android.presentation.ui.login.loginManager.KakaoLoginManager
import com.example.cherry_pick_android.presentation.ui.login.loginManager.NaverLoginManager
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity
import com.example.cherry_pick_android.presentation.util.PlatformManager
import com.example.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var kakaoLoginManager: KakaoLoginManager
    @Inject
    lateinit var naverLoginManager: NaverLoginManager

    private val viewModel: LoginViewModel by viewModels()
    private var loginFlag = false // 최초사용자 확인
    companion object{
        const val TAG = "LoginActivity"
        private const val KAKAO = "kakao"
        private const val NAVER = "naver"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 텍스트 스타일 설정
        binding.tvExplain.text = textToBold(binding.tvExplain.text.toString(), 7, 17)
        binding.tvExplain2.text = textToBold(binding.tvExplain2.text.toString(), 0, 16)

        onClockLogin()

        viewModel.token.observe(this@LoginActivity, Observer {
            if(it != ""){
                moveActivity()
                finish()
            }
        })

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

    private fun onClockLogin(){
        with(binding){
            linearKakaoLoginBtn.setOnClickListener {
                PlatformManager.setPlatform(KAKAO)
                isInitUser(KAKAO) // 최초사용자 구별
                kakaoLoginManager.startKakaoLogin {
                    viewModel.updateSocialToken(it)
                }
            }

            linearNaverLoginBtn.setOnClickListener {
                PlatformManager.setPlatform(NAVER)
                isInitUser(NAVER) // 최초사용자 구별
                naverLoginManager.startLogin {
                    viewModel.updateSocialToken(it)
                }
            }
        }
    }

    // 최초 사용자 여부에 따라 화면전환
    private fun moveActivity(){
        if(loginFlag){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this@LoginActivity, InformSettingActivity::class.java)
            startActivity(intent)
        }

    }

    private fun isInitUser(platform: String){
        if(platform == "kakao"){
            UserApiClient.instance.me { user, error ->
                Log.d(TAG, user?.id.toString())
                loginFlag = user?.id != null
            }
        }else{
            NidOAuthLogin().callProfileApi(object: NidProfileCallback<NidProfileResponse>{
                override fun onError(errorCode: Int, message: String) {}
                override fun onFailure(httpStatus: Int, message: String) {}
                override fun onSuccess(result: NidProfileResponse) {
                    Log.d(TAG, result.profile?.id.toString())
                    loginFlag = result.profile?.id != null
                }

            })
        }
    }
}