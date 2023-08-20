package com.example.cherry_pick_android.presentation.ui.newsSearch

import SearchRecordAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.model.SearchRecordEntity
import com.example.cherry_pick_android.databinding.ActivityNewsSearchBinding
import com.example.cherry_pick_android.presentation.viewmodel.searchRecord.SearchRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsSearchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewsSearchBinding
    private val manager = supportFragmentManager

    private val searchRecordViewModel: SearchRecordViewModel by viewModels()
    private lateinit var searchRecordAdapter: SearchRecordAdapter

    private lateinit var nowList : List<SearchRecordEntity>
    private var searchRecordLiveData: LiveData<List<SearchRecordEntity>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        searchRecordViewModel.loadRecord().observe(this) {searchList ->
            nowList = searchList
        }



        initFragment()
        changeText()
        goToBack()

        // 텍스트가 변경될 때마다 프래그먼트 변경 감지
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newText = s?.toString() ?: ""
                if (newText.isEmpty()) {
                    changeFragment(ArticleSearchFragment.oldInstance())
                }
            }
        })
    }

    override fun onDestroy() {
        searchRecordLiveData?.removeObservers(this) // 액티비티 종료 시 관찰 해제
        super.onDestroy()
    }

    // 초기 프래그먼트 선언
    private fun initFragment() {
        binding.etSearch.requestFocus()
        binding.ivDelete.setOnClickListener {
            binding.etSearch.text.clear()   // x 버튼 클릭시 텍스트 지우기
        }
        val transaction = manager.beginTransaction()
            .add(R.id.fl_search, ArticleSearchFragment())
        transaction.commit()
    }

    // 백 버튼 누르면 홈 화면으로
    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    // etSearch 텍스트 업데이트
    fun updateSearchText(keyword: String) {
        binding.etSearch.setText(keyword)
        binding.etSearch.setSelection(binding.etSearch.text.length) // 커서를 텍스트 끝으로 이동
    }

    fun getNowText(): String {
        return binding.etSearch.text.toString()
    }

    private fun changeText() {
        // 엔터 감지
        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val text = binding.etSearch.text.toString().trim()

                searchRecordViewModel.viewModelScope.launch {
                    if (text.isNotEmpty()) {

                        val existingRecord = nowList.find { it.record == text }
                        Log.d("검색어 리스트", "$nowList")

                        if (existingRecord != null) {
                            searchRecordViewModel.deleteRecord(existingRecord.record)
                            searchRecordViewModel.addRecord(text)
                        } else {
                            searchRecordViewModel.addRecord(text)
                        }

                        // ArticleSearchFragment를 SearchListFragment로 대체
                        val searchListFragment = SearchListFragment.newInstance()
                        changeFragment(searchListFragment)
                    } else {
                        val articleSearchFragment = ArticleSearchFragment.oldInstance()
                        changeFragment(articleSearchFragment)
                    }

                }
                   true
            } else {
                false
            }
        }
    }



    fun changeFragment(fragment: Fragment) {
        manager.beginTransaction().replace(R.id.fl_search, fragment).commit()
    }

}