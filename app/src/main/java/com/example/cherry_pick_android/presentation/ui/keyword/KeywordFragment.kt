package com.example.cherry_pick_android.presentation.ui.keyword

import android.content.Context
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
import com.example.cherry_pick_android.presentation.adapter.KeywordListAdapter
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
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    companion object{
        const val TAG = "keywordFragment"
        fun newInstance(): KeywordFragment = KeywordFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner) { keywordList ->
            keywordListAdapter.setList(keywordList)
            binding.tvKeywordCnt.text = keywordList.size.toString()

            // 만약 키워드가 존재하지 않다면 firstkeyword 프래그먼트로 이동
            if (keywordList.isEmpty()) {
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fv_home,
                            FirstKeywordFragment.newInstance(),
                            FirstKeywordFragment.TAG
                        )
                transaction.commitAllowingStateLoss()
            } else {
                // 처음 아이템 선택 처리
                keywordList[0].isSelected = true
                keywordListAdapter.notifyDataSetChanged()
            }
        }

        initView()

        // 키워드 검색 프래그먼트로 전환할 때 바텀네비게이션 뷰 비활성화
        bottomNavigationView = requireActivity().findViewById(R.id.btm_nav_view_home)
        binding.tvSearch.setOnClickListener {
            showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
            bottomNavigationView.isGone = true
        }

    }

    private fun initView(){
        binding.rvKeyword.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        keywordListAdapter = KeywordListAdapter(this)
        binding.rvKeyword.adapter = keywordListAdapter

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