package com.example.cherry_pick_android.presentation.ui.keyword

import android.content.Context
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.ArticleItem
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchKeywordService
import com.example.cherry_pick_android.databinding.FragmentKeywordBinding
import com.example.cherry_pick_android.presentation.adapter.KeywordListAdapter
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.first.FirstKeywordFragment
import com.example.cherry_pick_android.presentation.ui.keyword.search.SearchKeywordFragment
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class KeywordFragment : Fragment(), DeleteListener, AdapterInteractionListener {
    private val binding: FragmentKeywordBinding by lazy {
        FragmentKeywordBinding.inflate(layoutInflater)
    }
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var selectedKeyword: String


    @Inject
    lateinit var articleService: ArticleSearchKeywordService


    companion object {
        const val TAG = "keywordFragment"
        fun newInstance(): KeywordFragment = KeywordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        getArticleList("네이버")

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        keywordListAdapter = KeywordListAdapter(this, this)
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
                val firstKeyword = keywordList[0].keyword
                keywordList[0].isSelected = true
                keywordListAdapter.notifyDataSetChanged()

                // 기사 가져오기
                getArticleList(firstKeyword)
            }

            // 처음 아이템 선택 처리
            if (keywordList.isNotEmpty()) {
                selectedKeyword = keywordList[0].keyword
                loadArticlesByKeyword(selectedKeyword)
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

    private fun initView() {
        binding.rvKeyword.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        keywordListAdapter = KeywordListAdapter(this, this)
        binding.rvKeyword.adapter = keywordListAdapter
    }

    private fun getArticleList(keyword: String) {
        // API 통신
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val searchKeywordFragment = parentFragment as? SearchKeywordFragment
                val keyword = searchKeywordFragment?.getNowText().toString().trim()

                // trim으로 공백 제거
                val response = articleService.getArticleKeyword(
                    loginStatus = "",
                    sortType = "desc",
                    keyword = keyword,
                    pageable = Pageable(1, 10, "")
                )


                val statusCode = response.body()?.statusCode
                if (statusCode == 200) {
                    val articleItems = response.body()?.data?.content?.map { content ->
                        val imageUrl =
                            if (content.articlePhoto.isNotEmpty()) content.articlePhoto[0].articleImgUrl else "" // 기사 사진이 없으면 빈 문자열로 처리
                        ArticleItem(
                            content.title,
                            content.publisher,
                            content.uploadedAt,
                            imageUrl,
                            content.articleId
                        )
                    }?.toMutableList()
                    Log.d("기사", articleItems.toString())
                    binding.rvKeywordArticle.adapter = NewsRecyclerViewAdapter(articleItems)
                } else {
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDeleteClick(keyword: String) {
        searchKeywordViewModel.deleteKeyword(keyword)
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        bottomNavigationView.isVisible = true
        super.onResume()
    }

    private fun showFragment(fragment: Fragment, tag: String) {
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

    override fun onButtonSelected(button: String) {
        selectedKeyword = button
        loadArticlesByKeyword(button)
    }


    // 버튼 클릭시 뉴스 리스트 갱신
    private fun loadArticlesByKeyword(keyword: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val response = articleService.getArticleKeyword(
                    loginStatus = "",
                    sortType = "desc",
                    keyword = keyword,
                    pageable = Pageable(1, 10, "")
                )

                // 기사를 가져온 후에 아래와 같이 어댑터에 기사 리스트를 전달하여 갱신
                val articleItems = response.body()?.data?.content?.map { content ->
                    val imageUrl =
                        if (content.articlePhoto.isNotEmpty()) content.articlePhoto[0].articleImgUrl else ""
                    ArticleItem(
                        content.title,
                        content.publisher,
                        content.uploadedAt,
                        imageUrl,
                        content.articleId
                    )
                }?.toMutableList()
                binding.rvKeywordArticle.adapter = NewsRecyclerViewAdapter(articleItems)
            }
        }
    }
}