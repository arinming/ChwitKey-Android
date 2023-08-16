package com.example.cherry_pick_android.presentation.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.cherry_pick_android.databinding.FragmentMypageBinding
import com.example.cherry_pick_android.presentation.ui.login.LoginActivity
import com.example.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    companion object{
        const val TAG = "MyPageFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        setButton()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setButton() {
        binding.apply {
            // 다이얼로그로 로그아웃 기능 구현
            ibtnMypageLogout.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("알림")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃"){_,_->
                        loginViewModel.setUserData("userId", "")
                        loginViewModel.setUserData("platform", "")
                        loginViewModel.setIsOutView("out")

                        activity?.let {
                            val intent = Intent(it, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // 백스택에 남아있는 액티비티 제거
                            }
                            it.startActivity(intent)
                        }
                    }
                    .setNegativeButton("취소"){_, _->}
                    .create()
                    .show()
            }
            // 계정설정 (프로필) 페이지로 이동
            ibtnMypageSetting.setOnClickListener {
                activity?.let {
                    val intent = Intent(it, ProfileActivity::class.java)
                    it.startActivity(intent)
                }
            }

            // 스토어 별점 남기기 이동
            ibtnMoveToScore.setOnClickListener {
                // val intent = Intent(Intent.ACTION_VIEW)
                // intent.data = Uri.parse("market://details?id=$packageName")
                // requireContext().startActivity(intent)
            }
            // 이용약관 및 사용자 정책 페이지 이동
            ibtnMoveToPolicy.setOnClickListener {

            }
            // 언론사 연락처

            // 개인정보처리방침
        }
    }


}