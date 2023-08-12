package com.example.cherry_pick_android.presentation.ui.newsSearch

import SearchRecordAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.data.data.Keyword
import com.example.cherry_pick_android.data.data.SearchRecord
import com.example.cherry_pick_android.databinding.FragmentArticleSearchBinding
import com.example.cherry_pick_android.presentation.adapter.ArticleKeywordAdapter

class ArticleSearchFragment : Fragment() {
    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private val keywords = listOf(
        Keyword("2차전지"), Keyword("IT"), Keyword("철강"), Keyword("정유"),
        Keyword("석유"), Keyword("반도체"), Keyword("디스플레이"), Keyword("휴대폰"),
        Keyword("반도체"), Keyword("해운"), Keyword("F&B"), Keyword("건설"), Keyword("소매유통")
    )

    private val records = mutableListOf(
        SearchRecord(1, "검색어 1"), SearchRecord(2, "검색어 2"), SearchRecord(3, "검색어 3"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        deleteAll()

    }

    private fun initView() {
        // 키워드
        binding.rvSearchNewsList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSearchNewsList.adapter = ArticleKeywordAdapter(keywords)

        // 검색어
        binding.rvRecordList.layoutManager = LinearLayoutManager(context)
        binding.rvRecordList.adapter = SearchRecordAdapter(records) // MutableList 전달    }
    }

    private fun deleteAll() {
        // 모두 지우기 버튼 클릭 이벤트 설정
        binding.btnAllDelete.setOnClickListener {
            records.clear() // 검색어 아이템 모두 삭제
            binding.rvRecordList.adapter?.notifyDataSetChanged() // 어댑터에 변경 알림
        }
    }


}