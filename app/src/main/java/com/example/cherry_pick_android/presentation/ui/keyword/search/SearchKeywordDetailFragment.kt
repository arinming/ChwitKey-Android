package com.example.cherry_pick_android.presentation.ui.keyword.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.databinding.FragmentSearchKeywordDetailBinding
import com.example.cherry_pick_android.presentation.ui.keyword.dialog.KeywordDialog

class SearchKeywordDetailFragment: Fragment() {
    private val binding: FragmentSearchKeywordDetailBinding by lazy {
        FragmentSearchKeywordDetailBinding.inflate(layoutInflater)
    }
    companion object{
        const val TAG = "searchKeywordDetailFragment"
        fun newInstance(): SearchKeywordDetailFragment = SearchKeywordDetailFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getKeyword()
    }

    private fun getKeyword() {
        // 키워드 등록 버튼 이벤트
        binding.btnComplete.setOnClickListener {
//            val keyword = binding.etSearch.text.toString().trim() // EditText 내용 가져오기
//            val isKeywordNew = searchKeywordViewModel.checkKeyword(keyword) // 중복 키워드 검사
//            val isKeywordCnt = searchKeywordViewModel.checkKeywordCnt() // 키워드 개수 검사
//
//            if (keyword.isNotEmpty() && isKeywordNew && isKeywordCnt) {
//                searchKeywordViewModel.addKeyword(keyword)
//                hideKeyboard()
//                binding.etSearch.text = null
//                KeywordDialog().show(parentFragmentManager, "keyword_dialog")
//            } else if (keyword.isEmpty()) {
//                Toast.makeText(context, "키워드를 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
//            } else if (!isKeywordCnt) {
//                Toast.makeText(context, "키워드 최대 개수를 초과했습니다", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "이미 존재하는 키워드입니다", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}