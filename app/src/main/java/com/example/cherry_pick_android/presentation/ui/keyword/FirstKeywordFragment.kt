package com.example.cherry_pick_android.presentation.ui.keyword

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentFirstKeywordBinding

import com.example.cherry_pick_android.presentation.adapter.KeywordAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

// 키워드 등록이 안되어있는 경우의 프래그먼트
class FirstKeywordFragment : Fragment() {
    private val binding: FragmentFirstKeywordBinding by lazy {
        FragmentFirstKeywordBinding.inflate(layoutInflater)
    }
    private lateinit var bottomNavigationView: BottomNavigationView
    // 추천 키워드 추가
    private val keywords = listOf(
        Keyword("2차전지"), Keyword("IT"), Keyword("철강"), Keyword("정유"),
        Keyword("석유"), Keyword("반도체"), Keyword("디스플레이"), Keyword("휴대폰"),
        Keyword("반도체"), Keyword("해운"), Keyword("F&B"), Keyword("건설"),
        Keyword("소매유통"), Keyword("건설"), Keyword("철강"), Keyword("정유")
    )

    // 프래그먼트 인스턴스 및 TAG
    companion object{
        const val TAG = "homeFragment"
        fun newInstance(): FirstKeywordFragment = FirstKeywordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 어뎁터 연결 관련 함수 호출
        initView()

        // 특정 텍스트 부분 색상 변경
        val builder = SpannableStringBuilder(binding.tvKeywordTitle.text.toString())
        builder.setSpan(
            ForegroundColorSpan(Color.rgb(255, 79, 110)),
            10,
            13,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvKeywordTitle.text = builder

        // 키워드 검색 프래그먼트로 전환할 때 바텀네비게이션 뷰 비활성화
        bottomNavigationView = requireActivity().findViewById(R.id.btm_nav_view_home)
        binding.tvSearch.setOnClickListener {
            showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
            bottomNavigationView.isGone = true
        }

        return binding.root
    }

    // 전환 후 돌아올때 네비게이션 뷰 다시 활성화
    override fun onResume() {
        super.onResume()
        bottomNavigationView.isVisible = true
    }

    // 프래그먼트 전환 함수
    fun showFragment(fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.horizon_enter_front,
                    R.anim.none,
                    R.anim.none,
                    R.anim.horizon_exit_front
                )
                .remove(this)
                .add(R.id.fv_home, fragment, tag)
        transaction.addToBackStack(tag).commitAllowingStateLoss()
    }

    // 어뎁터 연결 함수
    fun initView(){
        binding.rvKeyword.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvKeyword.adapter = KeywordAdapter(keywords)
    }
}