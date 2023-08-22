package com.chwitkey.cherry_pick_android.presentation.ui.keyword.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chwitkey.cherry_pick_android.R
import com.chwitkey.cherry_pick_android.data.model.KeywordEntity
import com.chwitkey.cherry_pick_android.databinding.FragmentSearchKeywordBinding
import com.chwitkey.cherry_pick_android.presentation.adapter.SearchKeywordAdapter
import com.chwitkey.cherry_pick_android.presentation.ui.keyword.DeleteListener
import com.chwitkey.cherry_pick_android.presentation.ui.keyword.KeywordFragment
import com.chwitkey.cherry_pick_android.presentation.ui.keyword.first.FirstKeywordFragment
import com.chwitkey.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

// 키워드 검색 프래그먼트
@AndroidEntryPoint
class SearchKeywordFragment : Fragment(), DeleteListener {
    private val binding: FragmentSearchKeywordBinding by lazy {
        FragmentSearchKeywordBinding.inflate(layoutInflater)
    }
    var searchKeywordDetailFragment: SearchKeywordDetailFragment? = null
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels() // 뷰모델 초기화 불필요 (Hilt)
    private lateinit var searchKeywordAdapter: SearchKeywordAdapter
    private lateinit var nowKeyword : List<KeywordEntity>

    companion object {
        const val TAG = "SearchKeywordFragment"
        fun newInstance(): SearchKeywordFragment = SearchKeywordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.etSearch.requestFocus() // 자동으로 EditText에 커서 효과 적용
        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner) {searchList ->
            nowKeyword = searchList
        }

        initView() // 어뎁터 적용

        // EditText의 텍스트 변경 감지
        observeText()

        // DB 데이터 로드 및 개수 초기화
        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner) {
            searchKeywordAdapter.setList(it)
            binding.tvKeywordCnt.text = it.size.toString()
        }

        // 삭제 버튼 이벤트 적용 (입력한 값 지우기)
        binding.ivDelete.setOnClickListener {
            binding.etSearch.text = null
        }

        // 이전 버튼 이벤트 적용 (+ 키패드 내리기)
        binding.ivBack.setOnClickListener {
            hideKeyboard()
            requireActivity().supportFragmentManager.popBackStack()
        }



        return binding.root
    }

    // 키워드가 존재 유무에 따른 프래그먼트 전환
    override fun onDestroyView() {
        Log.d(TAG, "onDestoryView")
        if (searchKeywordViewModel.loadKeyword().value!!.isNotEmpty()) {
            showFragment(KeywordFragment.newInstance(), KeywordFragment.TAG)
        } else {
            showFragment(FirstKeywordFragment.newInstance(), FirstKeywordFragment.TAG)
        }
        super.onDestroyView()
    }


    // 검색 세부사항 프래그먼트 전환 함수
    private fun addDetailFragment() {
        if (searchKeywordDetailFragment == null) {
            searchKeywordDetailFragment = SearchKeywordDetailFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.fv_search_keyword, searchKeywordDetailFragment!!,
                    SearchKeywordDetailFragment.TAG
                )
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

    fun getNowText() : String {
        return binding.etSearch.text.toString()
    }

    // 키패드 제거 함수
    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
    }

    private fun initView() {
        binding.rvKeyword.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        searchKeywordAdapter = SearchKeywordAdapter(this)
        binding.rvKeyword.adapter = searchKeywordAdapter
    }


    // 키워드 제거 함수
    override fun onDeleteClick(keyword: String) {
        Log.d(TAG, "Delete")
        searchKeywordViewModel.deleteKeyword(keyword)
    }

    // 프래그먼트 전환 함수
    private fun showFragment(fragment: Fragment, tag: String) {
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fv_home, fragment, tag)
        transaction.commitAllowingStateLoss()
    }

    private fun changeText() {
        // 엔터 감지
        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val text = binding.etSearch.text.toString()
                if (text.isNotEmpty()) {
                    addDetailFragment()
                }
                true
            } else {
                false
            }
        }
    }

    private fun observeText(){
        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(p0.isNullOrEmpty()){
                    removeDetailFragment()
                }else{
                    changeText()
                }
            }

        })
    }

}