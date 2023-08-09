package com.example.cherry_pick_android.presentation.ui.keyword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.R

import com.example.cherry_pick_android.databinding.FragmentKeywordBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.adapter.SearchKeywordAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.first.FirstKeywordFragment
import com.example.cherry_pick_android.presentation.ui.keyword.search.SearchKeywordFragment
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordFragment : Fragment(), DeleteListener {
    private val binding: FragmentKeywordBinding by lazy {
        FragmentKeywordBinding.inflate(layoutInflater)
    }
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()
    private lateinit var searchKeywordAdapter: SearchKeywordAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private val news = listOf(
        News("뉴스1"), News("뉴스2"), News("뉴스3"), News("뉴스4"),
        News("뉴스5"), News("뉴스6"), News("뉴스7"), News("뉴스8"),
        News("뉴스9"), News("뉴스10"), News("뉴스11"), News("뉴스12")
    )
    companion object{
        const val TAG = "keywordFragment"
        fun newInstance(): KeywordFragment = KeywordFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()

        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner){
            searchKeywordAdapter.setList(it)
            binding.tvKeywordCnt.text = it.size.toString()

            // 만약 키워드가 존재하지 않다면 firstkeyword 프래그먼트로 이동
            if(it.isEmpty()){
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fv_home, FirstKeywordFragment.newInstance(), FirstKeywordFragment.TAG)
                transaction.commitAllowingStateLoss()
            }
        }

        // 키워드 검색 프래그먼트로 전환할 때 바텀네비게이션 뷰 비활성화
        bottomNavigationView = requireActivity().findViewById(R.id.btm_nav_view_home)
        binding.tvSearch.setOnClickListener {
            showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
            bottomNavigationView.isGone = true
        }

        return binding.root
    }

    private fun initView(){
        binding.rvKeyword.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        searchKeywordAdapter = SearchKeywordAdapter(this)
        binding.rvKeyword.adapter = searchKeywordAdapter

        binding.rvKeywordArticle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvKeywordArticle.adapter = NewsRecyclerViewAdapter(news)
    }

    override fun onDeleteClick(keyword: String) {
        searchKeywordViewModel.deleteKeyword(keyword)
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        bottomNavigationView.isVisible = true
        super.onResume()
    }

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

}