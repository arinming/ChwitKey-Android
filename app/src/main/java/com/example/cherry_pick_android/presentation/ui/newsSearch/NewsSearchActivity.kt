package com.example.cherry_pick_android.presentation.ui.newsSearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.Keyword
import com.example.cherry_pick_android.data.data.SearchRecord
import com.example.cherry_pick_android.databinding.ActivityNewsSearchBinding
import com.example.cherry_pick_android.presentation.ui.home.homeNews.HomeNewsFragment
import com.example.cherry_pick_android.presentation.viewmodel.searchRecord.SearchRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSearchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewsSearchBinding
    private val manager = supportFragmentManager

    private val searchRecordViewModel: SearchRecordViewModel by viewModels()

    companion object {
        const val TAG = "NewsSearchActivity"
        fun newInstance(): NewsSearchActivity = NewsSearchActivity()
    }

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

        initFragment()

        goToBack()
    }


    // 초기 프래그먼트 선언
    private fun initFragment() {
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

    private fun changeText() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isNotEmpty()) {

                }
            }
        })
    }

    private fun addDetailFragment() {

    }
}