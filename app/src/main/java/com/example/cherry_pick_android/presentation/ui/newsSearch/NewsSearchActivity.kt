package com.example.cherry_pick_android.presentation.ui.newsSearch

import SearchRecordAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.data.data.Keyword
import com.example.cherry_pick_android.data.data.SearchRecord
import com.example.cherry_pick_android.databinding.ActivityNewsSearchBinding
import com.example.cherry_pick_android.presentation.adapter.ArticleKeywordAdapter
import com.example.cherry_pick_android.presentation.ui.searchList.SearchListActivity

class NewsSearchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewsSearchBinding

    private val keywords = listOf(
        Keyword("2차전지"), Keyword("IT"), Keyword("철강"), Keyword("정유"),
        Keyword("석유"), Keyword("반도체"), Keyword("디스플레이"), Keyword("휴대폰"),
        Keyword("반도체"), Keyword("해운"), Keyword("F&B"), Keyword("건설"), Keyword("소매유통")
    )

    private val records = mutableListOf(
        SearchRecord(1, "검색어 1"), SearchRecord(2, "검색어 2"), SearchRecord(3, "검색어 3"),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()

        goToBack()
        allDelete()
        goToSearchList()
    }

    // 백 버튼 누르면 홈 화면으로
    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun goToSearchList() {
        binding.btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchListActivity::class.java))
        }
    }

    private fun initView() {
        // 키워드
        binding.rvSearchNewsList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSearchNewsList.adapter = ArticleKeywordAdapter(keywords)

        // 검색어
        binding.rvRecordList.layoutManager = LinearLayoutManager(this)
        binding.rvRecordList.adapter = SearchRecordAdapter(records) // MutableList 전달    }
    }

    private fun allDelete() {
        // 모두 지우기 버튼 클릭 이벤트 설정
        binding.btnAllDelete.setOnClickListener {
            records.clear() // 검색어 아이템 모두 삭제
            binding.rvRecordList.adapter?.notifyDataSetChanged() // 어댑터에 변경 알림
        }
    }
}