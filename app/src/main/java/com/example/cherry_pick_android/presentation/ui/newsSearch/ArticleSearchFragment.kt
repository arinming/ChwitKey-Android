package com.example.cherry_pick_android.presentation.ui.newsSearch

import SearchRecordAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.data.data.Keyword
import com.example.cherry_pick_android.databinding.FragmentArticleSearchBinding
import com.example.cherry_pick_android.presentation.adapter.ArticleKeywordAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.AdapterInteractionListener
import com.example.cherry_pick_android.presentation.ui.keyword.AddListener
import com.example.cherry_pick_android.presentation.ui.keyword.DeleteListener
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import com.example.cherry_pick_android.presentation.viewmodel.searchRecord.SearchRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleSearchFragment : Fragment(), AddListener, DeleteListener, AdapterInteractionListener {
    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()
    private val searchRecordViewModel: SearchRecordViewModel by viewModels()
    private lateinit var searchRecordAdapter: SearchRecordAdapter
    private var search = ""

    private val keywords = listOf(
        Keyword("2차전지"), Keyword("IT"), Keyword("철강"), Keyword("정유"),
        Keyword("석유"), Keyword("반도체"), Keyword("디스플레이"), Keyword("휴대폰"),
        Keyword("해운"), Keyword("F&B"), Keyword("건설"), Keyword("소매유통")
    )

    companion object {
        fun oldInstance(): ArticleSearchFragment = ArticleSearchFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        deleteAll()

        searchRecordViewModel.loadRecord().observe(viewLifecycleOwner) {
            searchRecordAdapter.setList(it)
        }

    }

    private fun initView() {
        // 키워드
        binding.rvSearchNewsList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSearchNewsList.adapter = ArticleKeywordAdapter(keywords, this)

        // 검색어
        binding.rvRecordList.layoutManager = LinearLayoutManager(context)
        searchRecordAdapter = SearchRecordAdapter(this, this@ArticleSearchFragment)
        binding.rvRecordList.adapter = searchRecordAdapter
    }

    private fun deleteAll() {
        // 모두 지우기 버튼 클릭 이벤트 설정
        binding.btnAllDelete.setOnClickListener {
            val recordsToDelete = searchRecordAdapter.getRecords() // 현재 레코드 리스트 가져오기
            for (record in recordsToDelete) {
                searchRecordViewModel.deleteRecord(record.record)
            }
        }
    }


    // 검색 기록 제거 함수
    override fun onDeleteClick(record: String) {
        searchRecordViewModel.deleteRecord(record)
    }

    // 추천 키워드 클릭 이벤트
    override fun onAddClick(keyword: String) {
        searchKeywordViewModel.viewModelScope.launch {
            val existingRecords = searchRecordAdapter.getRecords()
            val existingRecord = existingRecords.find { it.record == keyword }

            Log.d("현재 검색어 리스트", "$existingRecords")
            if (existingRecord != null) {
                // 이미 존재하는 키워드를 리스트에서 삭제하고, 맨 앞에 새로운 값 추가
                searchRecordViewModel.deleteRecord(existingRecord.record)
                searchRecordAdapter.removeRecord(existingRecord)
                searchRecordViewModel.addRecord(keyword)

                // NewsSearchActivity의 etSearch 텍스트 변경
                if (activity is NewsSearchActivity) {
                    val newsSearchActivity = activity as NewsSearchActivity
                    newsSearchActivity.updateSearchText(keyword)
                }
            } else {
                // 새로운 값만 추가
                searchRecordViewModel.addRecord(keyword)
                // NewsSearchActivity의 etSearch 텍스트 변경
                if (activity is NewsSearchActivity) {
                    val newsSearchActivity = activity as NewsSearchActivity
                    newsSearchActivity.updateSearchText(keyword)
                }
            }
        }
        val newsSearchActivity = activity as? NewsSearchActivity
        newsSearchActivity?.changeFragment(SearchListFragment.newInstance())
    }

    override fun onButtonSelected(button: String) {
        searchKeywordViewModel.viewModelScope.launch {
            val existingRecords = searchRecordAdapter.getRecords()
            val existingRecord = existingRecords.find { it.record == button}

            Log.d("현재 검색어 리스트", "$existingRecords")
            if (existingRecord != null) {
                // 이미 존재하는 키워드를 리스트에서 삭제하고, 맨 앞에 새로운 값 추가
                searchRecordViewModel.deleteRecord(existingRecord.record)
                searchRecordAdapter.removeRecord(existingRecord)
                searchRecordViewModel.addRecord(button)

                // NewsSearchActivity의 etSearch 텍스트 변경
                if (activity is NewsSearchActivity) {
                    val newsSearchActivity = activity as NewsSearchActivity
                    newsSearchActivity.updateSearchText(button)
                }
            } else {
                // 새로운 값만 추가
                searchRecordViewModel.addRecord(button)
                // NewsSearchActivity의 etSearch 텍스트 변경
                if (activity is NewsSearchActivity) {
                    val newsSearchActivity = activity as NewsSearchActivity
                    newsSearchActivity.updateSearchText(button)
                }
            }
        }
        val newsSearchActivity = activity as? NewsSearchActivity
        newsSearchActivity?.changeFragment(SearchListFragment.newInstance())

    }
}