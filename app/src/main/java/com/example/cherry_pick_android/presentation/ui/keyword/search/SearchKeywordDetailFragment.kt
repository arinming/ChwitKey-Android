package com.example.cherry_pick_android.presentation.ui.keyword.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentSearchKeywordDetailBinding
import com.example.cherry_pick_android.presentation.adapter.SearchKeywordAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.DeleteListener
import com.example.cherry_pick_android.presentation.ui.keyword.dialog.KeywordDialog
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchKeywordDetailFragment: Fragment() {
    private val binding: FragmentSearchKeywordDetailBinding by lazy {
        FragmentSearchKeywordDetailBinding.inflate(layoutInflater)
    }
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels() // 뷰모델 초기화 불필요 (Hilt)
    private lateinit var searchKeywordAdapter: SearchKeywordAdapter

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

        // DB 데이터 로드 및 개수 초기화
        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner) {
            searchKeywordAdapter.setList(it)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getKeyword()

    }

    private fun getKeyword() {
        // 키워드 등록 버튼 이벤트
        binding.btnComplete.setOnClickListener {
            val searchKeywordFragment = parentFragment as? SearchKeywordFragment
            val keyword = searchKeywordFragment?.getNowText().toString().trim()

            val isKeywordNew = searchKeywordViewModel.checkKeyword(keyword) // 중복 키워드 검사
            val isKeywordCnt = searchKeywordViewModel.checkKeywordCnt() // 키워드 개수 검사

            if (keyword.isNotEmpty() && isKeywordCnt) {
                if (isKeywordNew) {
                    searchKeywordViewModel.addKeyword(keyword)
                    hideKeyboard()
                    KeywordDialog().show(parentFragmentManager, "keyword_dialog")
                    showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
                } else {
                    Toast.makeText(context, "이미 존재하는 키워드입니다", Toast.LENGTH_SHORT).show()
                }
            } else if (keyword.isEmpty()) {
                Toast.makeText(context, "키워드를 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            } else if (!isKeywordCnt) {
                Toast.makeText(context, "키워드 최대 개수를 초과했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }



    // 키패드 제거 함수
    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
    }

    private fun showFragment(fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.horizon_enter_front,
                    R.anim.none,
                    R.anim.none,
                    R.anim.horizon_exit_front
                )
                .add(R.id.fv_home, fragment, tag)
        transaction.addToBackStack(tag).commitAllowingStateLoss()
    }
}