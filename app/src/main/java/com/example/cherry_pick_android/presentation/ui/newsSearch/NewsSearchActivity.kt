package com.example.cherry_pick_android.presentation.ui.newsSearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.model.SearchRecordEntity
import com.example.cherry_pick_android.databinding.ActivityNewsSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSearchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewsSearchBinding
    private val manager = supportFragmentManager

    private var searchRecordLiveData: LiveData<List<SearchRecordEntity>>? = null

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
        // 엔터 감지
        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val text = binding.etSearch.text.toString()
                if (text.isNotEmpty()) {
                    // ArticleSearchFragment를 SearchListFragment로 대체
                    val searchListFragment = SearchListFragment.newInstance()
                    manager.beginTransaction().replace(R.id.fl_search, searchListFragment).commit()
                }
                true
            } else {
                false
            }
        }
    }


    private fun changeFragment(fragment: Fragment) {
        manager.beginTransaction().replace(R.id.fl_search, fragment).commit()
    }

}