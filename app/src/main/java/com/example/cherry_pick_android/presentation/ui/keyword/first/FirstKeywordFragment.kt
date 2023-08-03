package com.example.cherry_pick_android.presentation.ui.keyword.first

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentFirstKeywordBinding

import com.example.cherry_pick_android.presentation.adapter.KeywordAdapter
import com.example.cherry_pick_android.presentation.adapter.SearchKeywordAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.AddListener
import com.example.cherry_pick_android.presentation.ui.keyword.DeleteListener
import com.example.cherry_pick_android.presentation.ui.keyword.Keywords
import com.example.cherry_pick_android.presentation.ui.keyword.dialog.DotLoadingFragment
import com.example.cherry_pick_android.presentation.ui.keyword.search.SearchKeywordFragment
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 키워드 등록이 안되어있는 경우의 프래그먼트
@AndroidEntryPoint
class FirstKeywordFragment : Fragment(), AddListener, DeleteListener {
    private val binding: FragmentFirstKeywordBinding by lazy {
        FragmentFirstKeywordBinding.inflate(layoutInflater)
    }
    private lateinit var bottomNavigationView: BottomNavigationView
    private val keywords = Keywords.keywords // 추천 키워드 추가
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()
    private lateinit var searchKeywordAdapter: SearchKeywordAdapter

    // 프래그먼트 인스턴스 및 TAG
    companion object{
        const val TAG = "firstKeywordFragment"
        fun newInstance(): FirstKeywordFragment = FirstKeywordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        initView() // 어뎁터 연결 관련 함수 호출

        // 특정 텍스트 부분 색상 변경
        val builder = SpannableStringBuilder(binding.tvKeywordTitle.text.toString())
        builder.setSpan(
            ForegroundColorSpan(Color.rgb(255, 79, 110)),
            10,
            13,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvKeywordTitle.text = builder

        // 키워드 검색 프래그먼트로 전환할 때 바텀네비게이션 뷰 비활성화
        bottomNavigationView = requireActivity().findViewById(R.id.btm_nav_view_home)
        binding.tvSearch.setOnClickListener {
            showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
            bottomNavigationView.isGone = true
        }

        // searchKeywordAdapter에 적용하기 위한 코드
        searchKeywordAdapter = SearchKeywordAdapter(this)
        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner){
            searchKeywordAdapter.setList(it)
        }

        return binding.root
    }

    // 전환 후 돌아올때 네비게이션 뷰 다시 활성화
    override fun onResume() {
        Log.d(TAG, "onResume")
        bottomNavigationView.isVisible = true
        super.onResume()
    }

    // 프래그먼트 전환 함수
    fun showFragment(fragment: Fragment, tag: String){
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.horizon_enter_front,
                    R.anim.none,
                    R.anim.none,
                    R.anim.horizon_exit_front
                )
                .remove(this)
                .add(R.id.fv_home, fragment, tag)
        transaction.addToBackStack(tag).commitAllowingStateLoss()
    }

    // 어뎁터 연결 함수
    fun initView(){
        binding.rvKeyword.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvKeyword.adapter = KeywordAdapter(keywords, this)
    }

    // 추천 키워드 클릭 이벤트
    override fun onAddClick(keyword: String) {
        searchKeywordViewModel.viewModelScope.launch {
            val isKeywordNew = searchKeywordViewModel.checkKeyword(keyword) // 키워드 중복 검사
            val isKeywordCnt = searchKeywordViewModel.checkKeywordCnt() // 키워드 개수 검사

            if(isKeywordNew && isKeywordCnt){
                searchKeywordViewModel.addKeyword(keyword)
                showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
                bottomNavigationView.isGone = true
                Toast.makeText(context, "$keyword 키워드가 추가되었습니다", Toast.LENGTH_SHORT).show()
            }
            else if(!isKeywordCnt){
                Toast.makeText(context, "키워드 최대 개수를 초과했습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "이미 존재하는 키워드입니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 구현필요 x
    override fun onDeleteClick(keyword: String) {}

}