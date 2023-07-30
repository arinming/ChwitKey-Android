package com.example.cherry_pick_android.presentation.ui.newsSearch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.databinding.ActivityNewsSearchBinding
import com.example.cherry_pick_android.presentation.adapter.KeywordAdapter
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.keyword.Keyword
import com.example.cherry_pick_android.presentation.ui.searchList.SearchListActivity

class NewsSearchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewsSearchBinding

    private val keywords = listOf(
        Keyword("2차전지"), Keyword("IT"), Keyword("철강"), Keyword("정유"),
        Keyword("석유"), Keyword("반도체"), Keyword("디스플레이"), Keyword("휴대폰"),
        Keyword("반도체"), Keyword("해운"), Keyword("F&B"), Keyword("건설"),
        Keyword("소매유통"), Keyword("건설"), Keyword("철강"), Keyword("정유")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()

        goToBack()
        goToSearchList()
    }

    // 백 버튼 누르면 홈 화면으로
    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun goToSearchList() {
        binding.btnAllDelete.setOnClickListener {
            startActivity(Intent(this, SearchListActivity::class.java))
            finish()
        }
    }

    fun initView(){
        binding.rvSearchNewsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSearchNewsList.adapter = KeywordAdapter(keywords)
    }
}