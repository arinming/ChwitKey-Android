package com.example.cherry_pick_android.presentation.ui.newsSearch

import SearchRecordAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.SearchRecord
import com.example.cherry_pick_android.data.model.SearchRecordEntity
import com.example.cherry_pick_android.databinding.ActivityNewsSearchBinding
import com.example.cherry_pick_android.presentation.viewmodel.searchRecord.SearchRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSearchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewsSearchBinding
    private val manager = supportFragmentManager

    private var searchRecordLiveData: LiveData<List<SearchRecordEntity>>? = null

    private val records = mutableListOf(
        SearchRecord(1, "검색어 1"), SearchRecord(2, "검색어 2"), SearchRecord(3, "검색어 3"),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initFragment()
        changeText()
        goToBack()
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

    private fun changeText() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isNotEmpty()) {
                    SearchListFragment().changeFragment()
                } else {
                    ArticleSearchFragment().changeFragment()
                }
            }
        })
    }


    // 프래그먼트 전환 작업
    private fun Fragment.changeFragment() {
        manager.beginTransaction().replace(R.id.fl_search, this).commit()
    }
}