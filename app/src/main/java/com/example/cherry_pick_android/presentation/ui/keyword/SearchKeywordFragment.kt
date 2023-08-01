package com.example.cherry_pick_android.presentation.ui.keyword

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentSearchKeywordBinding
import com.example.cherry_pick_android.presentation.adapter.SearchKeywordAdapter
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

// 키워드 검색 프래그먼트
@AndroidEntryPoint
class SearchKeywordFragment: Fragment(), DeleteListener {
    private val bindig: FragmentSearchKeywordBinding by lazy {
        FragmentSearchKeywordBinding.inflate(layoutInflater)
    }
    private var searchKeywordDetailFragment: SearchKeywordDetailFragment? = null
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels() // 뷰모델 초기화 불필요 (Hilt)
    private lateinit var searchKeywordAdapter: SearchKeywordAdapter
    companion object{
        const val TAG = "SearchKeywordFragment"
        fun newInstance(): SearchKeywordFragment = SearchKeywordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindig.etSearch.requestFocus() // 자동으로 EditText에 커서 효과 적용

        initView() // 어뎁터 적용

        // EditText의 텍스트 변경 감지
        bindig.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isNotEmpty()) { // 입력하는 순간 검색 세부사항 프래그먼트로 이동
                    addDetailFragment()
                } else { // EditText값이 없다면 세부사항 프래그먼트 삭제
                    removeDetailFragment()
                }
            }
        })

        // DB 데이터 로드 및 개수 초기화
        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner){
            searchKeywordAdapter.setList(it)
            bindig.tvKeywordCnt.text = it.size.toString()
        }

        // 삭제 버튼 이벤트 적용 (입력한 값 지우기)
        bindig.ivDelete.setOnClickListener {
            bindig.etSearch.text = null
        }

        // 이전 버튼 이벤트 적용 (+ 키패드 내리기)
        bindig.ivBack.setOnClickListener {
            hideKeyboard()
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 키워드 등록 버튼 이벤트
        bindig.btnComplete.setOnClickListener {
            val keyword = bindig.etSearch.text.toString().trim() // EditText 내용 가져오기
            val isKeywordNew = searchKeywordViewModel.checkKeyword(keyword) // 중복 키워드 검사
            val isKeywordCnt = searchKeywordViewModel.checkKeywordCnt() // 키워드 개수 검사

            if(keyword.isNotEmpty() && isKeywordNew && isKeywordCnt){
                searchKeywordViewModel.addKeyword(keyword)
                hideKeyboard()
                bindig.etSearch.text = null
                KeywordDialog().show(parentFragmentManager, "keyword_dialog")
            }
            else if(keyword.isEmpty()){
                Toast.makeText(context, "키워드를 입력하지 않았습니다",Toast.LENGTH_SHORT).show()
            }
            else if(!isKeywordCnt){
                Toast.makeText(context, "키워드 최대 개수를 초과했습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "이미 존재하는 키워드입니다", Toast.LENGTH_SHORT).show()
            }
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
    private fun initView(){
        bindig.rvKeyword.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        searchKeywordAdapter = SearchKeywordAdapter(this)
        bindig.rvKeyword.adapter = searchKeywordAdapter
    }

    // 키워드 제거 함수
    override fun onDeleteClick(keyword: String) {
        Log.d(TAG, "Delete")
        searchKeywordViewModel.deleteKeyword(keyword)
    }

}