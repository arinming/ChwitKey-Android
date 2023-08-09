package com.example.cherry_pick_android.presentation.ui.mypage

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
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
            // 계정설정 (프로필) 페이지로 이동
            ibtnMypageToProfile.setOnClickListener{
                val intent = Intent(it.context,ProfileActivity::class.java)
                it.context.startActivity(intent)
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