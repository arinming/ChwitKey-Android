package com.example.cherry_pick_android.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityHomeBinding
import com.example.cherry_pick_android.presentation.ui.home.homeNews.HomeNewsFragment
import com.example.cherry_pick_android.presentation.ui.keyword.KeywordFragment
import com.example.cherry_pick_android.presentation.ui.home.scrap.ScrapFragment
import com.example.cherry_pick_android.presentation.ui.keyword.first.FirstKeywordFragment
import com.example.cherry_pick_android.presentation.ui.mypage.MyPageFragment
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    // 뷰 바인딩
    private lateinit var binding: ActivityHomeBinding

    // 프래그먼트 매니저
    val mananger = supportFragmentManager

    // 뷰 모델 가져오기
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        searchKeywordViewModel.loadKeyword().observe(this){} // DB 업데이트
        initFragment()
        initBottomNavigation()
    }

    // 바텀 네비게이션으로 프래그먼트 간 화면 전환
    private fun initBottomNavigation() {

        // 각 아이콘을 눌렀을 때 작용
        binding.btmNavViewHome.setOnItemSelectedListener {
            when(it.itemId) {
                // 뉴스
                R.id.nav_fragment_home_news -> {
                    HomeNewsFragment().changeFragment()
                }
                // 조건에 맞는 키워드 프래그먼트로 이동
                R.id.nav_fragment_keyword -> {
                    if(searchKeywordViewModel.loadKeyword().value!!.isNotEmpty()){
                        KeywordFragment().changeFragment()
                    }else{
                        FirstKeywordFragment().changeFragment()
                    }
                }
                // 스크랩
                R.id.nav_fragment_scrap -> {
                    ScrapFragment().changeFragment()
                }
                // 마이페이지
                R.id.nav_fragment_my_page -> {
                    MyPageFragment().changeFragment()
                }

            }
            return@setOnItemSelectedListener true
        }
    }

    // 프래그먼트 전환 작업
    private fun Fragment.changeFragment() {
        mananger.beginTransaction().replace(R.id.fv_home, this).commit()
    }

    // 초기 프래그먼트 선언
    fun initFragment() {
        val transaction = mananger.beginTransaction()
            .add(R.id.fv_home, HomeNewsFragment()) // 뉴스 프래그먼트로 초기화
        transaction.commit()
    }

}