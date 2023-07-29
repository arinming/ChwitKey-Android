package com.example.cherry_pick_android.presentation.ui.keyword

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentSearchKeywordBinding

// 키워드 검색 프래그먼트
class SearchKeywordFragment: Fragment() {
    private val bindig: FragmentSearchKeywordBinding by lazy {
        FragmentSearchKeywordBinding.inflate(layoutInflater)
    }
    private var searchKeywordDetailFragment: SearchKeywordDetailFragment? = null
    companion object{
        const val TAG = "SearchKeywordFragment"
        fun newInstance(): SearchKeywordFragment = SearchKeywordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindig.etSearch.requestFocus() // 자동으로 EditText에 커서 효과 적용

        // EditText의 텍스트 변경 감지
        bindig.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isNotEmpty()) { // 입력하는 순간 검색 세부사항 프래그먼트로 이동
                    addDetailFragment()
                } else { // EditText값이 없다면 세부사항 프래그먼트 삭제
                    removeDetailFragment()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        // 삭제 버튼 이벤트 적용 (입력한 값 지우기)
        bindig.ivDelete.setOnClickListener {
            bindig.etSearch.text = null
        }

        // 이전 버튼 이벤트 적용 (+ 키패드 내리기)
        bindig.ivBack.setOnClickListener {
            hideKeyboard()
            requireActivity().supportFragmentManager.popBackStack()
        }
        
        return bindig.root
    }

    // 검색 세부사항 프래그먼트 전환 함수
    private fun addDetailFragment() {
        if (searchKeywordDetailFragment == null) {
            searchKeywordDetailFragment = SearchKeywordDetailFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(R.id.fv_search_keyword, searchKeywordDetailFragment!!, SearchKeywordDetailFragment.TAG)
                .commitAllowingStateLoss()
        }
    }

    // 검색 세부사항 프래그먼트 제거 함수
    private fun removeDetailFragment() {
        if (searchKeywordDetailFragment != null) {
            childFragmentManager.beginTransaction()
                .remove(searchKeywordDetailFragment!!)
                .commitAllowingStateLoss()
            searchKeywordDetailFragment = null
        }
    }

    // 키패드 제거 함수
    private fun hideKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
    }
}