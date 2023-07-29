package com.example.cherry_pick_android.presentation.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityHomeBinding
import com.example.cherry_pick_android.presentation.ui.home.homeNews.HomeNewsFragment
import com.example.cherry_pick_android.presentation.ui.keyword.KeywordFragment
import com.example.cherry_pick_android.presentation.ui.home.scrap.ScrapFragment
import com.example.cherry_pick_android.presentation.ui.keyword.FirstKeywordFragment
import com.example.cherry_pick_android.presentation.ui.mypage.MyPageFragment

class HomeActivity: AppCompatActivity() {
    // 뷰 바인딩
    private lateinit var binding: ActivityHomeBinding

    // 프래그먼트 매니저
    val mananger = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initFragment()
        initBottomNavigation()
    }

    // 바텀 네비게이션으로 프래그먼트 간 화면 전환
    private fun initBottomNavigation() {
        binding.btmNavViewHome.itemIconTintList = null

        // 각 아이콘을 눌렀을 때 작용
        binding.btmNavViewHome.setOnItemSelectedListener {
            when(it.itemId) {
                // 뉴스
                R.id.nav_fragment_home_news -> {
                    HomeNewsFragment().changeFragment()
                }
                // 키워드
                R.id.nav_fragment_keyword -> {
                    FirstKeywordFragment().changeFragment()
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