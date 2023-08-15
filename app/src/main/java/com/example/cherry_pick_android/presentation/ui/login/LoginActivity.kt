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
import com.example.cherry_pick_android.data.remote.request.login.SignInRequest
import com.example.cherry_pick_android.data.remote.service.login.SignInService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.data.remote.service.user.UserKeywordService
import com.example.cherry_pick_android.databinding.ActivityLoginBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
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
    lateinit var signInService: SignInService
    @Inject
    lateinit var userDataRepository: UserDataRepository

    private val viewModel: LoginViewModel by viewModels()
    private var flag = false

    companion object{
        const val TAG = "LoginActivity"
        private const val KAKAO = "kakao"
        private const val NAVER = "naver"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        /*viewModel.setUserData("userId", "")
        viewModel.setUserData("token", "")*/

        // 기존 회원 여부 검사
        viewModel.isLogin.observe(this@LoginActivity, Observer {
            if(it){
                lifecycleScope.launch {
                    val request = SignInRequest(
                        memberNumber = userDataRepository.getUserData().userId,
                        provider = userDataRepository.getUserData().platform
                    )
                    Log.d(TAG, "memNum: ${userDataRepository.getUserData().userId} plat: ${userDataRepository.getUserData().platform}")

                    val saveUserResponse = signInService.signInInform(request)
                    val response = saveUserResponse.body()?.data
                    Log.d(TAG, "response:${response}")

                    withContext(Dispatchers.Main){
                        if(response?.isMember.toString() == "true"){
                            viewModel.setUserData("isInit", "exitUser")
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else if(response?.isMember.toString() == "false"){
                            viewModel.setUserData("isInit", "InitUser")
                            viewModel.setUserData("token", response?.access_token.toString())
                            val intent = Intent(this@LoginActivity, InformSettingActivity::class.java)
                            startActivity(intent)
                            Log.d(TAG, "토큰 등록 완료")
                            finish()
                        }else{
                            Log.d(TAG, "ERROR:${saveUserResponse.body()?.status}")
                        }
                    }
                }
                flag = true
            }
        })

        Log.d(TAG, userDataRepository.getTokenLiveData().value.toString())


        // 소셜로그인 창을 닫은 후에도 무반응일 경우 수행하는 코드
        if(flag && !isFinishing){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 텍스트 스타일 설정
        binding.tvExplain.text = textToBold(binding.tvExplain.text.toString(), 7, 17)
        binding.tvExplain2.text = textToBold(binding.tvExplain2.text.toString(), 0, 16)


        onClickLogin()

        // 자동로그인 설정
        lifecycleScope.launch {
            Log.d(TAG, userDataRepository.getUserData().toString())
            if(userDataRepository.getUserData().token.isNotEmpty()){
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
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
                    UserApiClient.instance.me { user, error ->
                        viewModel.setUserData("userId", user?.id.toString())
                        viewModel.setUserData("platform", "kakao")
                        Log.d(TAG, "userId:${user?.id.toString()}")
                    }
                    viewModel.setIsLogin(true)
                }
            }

            linearNaverLoginBtn.setOnClickListener {
                PlatformManager.setPlatform(NAVER)
                naverLoginManager.startLogin {
                    NidOAuthLogin().callProfileApi(object: NidProfileCallback<NidProfileResponse>{
                        override fun onError(errorCode: Int, message: String) {}
                        override fun onFailure(httpStatus: Int, message: String) {}
                        override fun onSuccess(result: NidProfileResponse) {
                            viewModel.setUserData("userId", result.profile?.id.toString())
                            viewModel.setUserData("platform", "naver")
                            Log.d(TAG, "userId:${result.profile?.id}")
                        }
                    })

                    viewModel.setIsLogin(true)
                }
            }
        }
    }


}