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
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.ArticleItem
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchKeywordService
import com.example.cherry_pick_android.databinding.FragmentKeywordBinding
import com.example.cherry_pick_android.presentation.adapter.KeywordListAdapter
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.first.FirstKeywordFragment
import com.example.cherry_pick_android.presentation.ui.keyword.search.SearchKeywordFragment
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private var isDone = false
    private var pageInit: Int = 0
    private var isLoading = false
    private lateinit var mRecyclerView: RecyclerView

    private var articleOldItems = mutableListOf<ArticleItem>()
    private var savedScrollPosition: Int = 0

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

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        keywordListAdapter = KeywordListAdapter(this, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initScrollListener()

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

        }


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
        Log.d("키워드", keyword)

        var nowKeyword = keyword

        // API 통신

        lifecycleScope.launch {

            // trim으로 공백 제거
            val response = articleService.getArticleKeyword(
                sortType = "desc",
                keyword = nowKeyword,
                page = pageInit
            )

            val statusCode = response.body()?.statusCode

            withContext(Dispatchers.Main) {
                if (statusCode == 200) {

                    val articleItems = response.body()?.data?.content?.map { content ->
                        val imageUrl = if (content.articlePhoto.isNotEmpty()) content.articlePhoto[0].articleImgUrl else ""
                        ArticleItem(
                            content.title,
                            content.publisher,
                            content.uploadedAt,
                            imageUrl,
                            content.articleId
                        )
                    }?.toMutableList()

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
        articleOldItems.clear()
        selectedKeyword = button
        pageInit = 0
        loadArticlesByKeyword()
    }


    // 버튼 클릭시 뉴스 리스트 갱신
    private fun loadArticlesByKeyword() {
        savedScrollPosition = (binding.rvKeywordArticle.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        pageInit = 0

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val response = articleService.getArticleKeyword(
                    sortType = "desc",
                    keyword = selectedKeyword,
                    page = pageInit
                )
                Log.d("페이지", pageInit.toString())

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
                (binding.rvKeywordArticle.layoutManager as LinearLayoutManager).scrollToPosition(savedScrollPosition)

            }
        }
    }


    private fun initScrollListener() {
        binding.rvKeywordArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && !isDone && lastVisibleItemPosition == totalItemCount - 1) {
                    moreArticles(selectedKeyword)
                    isLoading = true
                }
            }
        })
    }


    fun moreArticles(keyword: String) {
        if (isLoading) return // 이미 로딩 중이라면 중복 호출 방지

        isLoading = true // 로딩 상태를 true로 설정

        mRecyclerView = binding.rvKeywordArticle

        // 페이지 번호를 증가시키고 새로운 기사를 로드
        pageInit++
        Log.d("키워드", keyword)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // 임의의 딜레이 추가
            // 이전 스크롤 위치 저장

            savedScrollPosition =
                (binding.rvKeywordArticle.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            lifecycleScope.launch {
                val response = articleService.getArticleKeyword(
                    sortType = "desc",
                    keyword = keyword,
                    page = pageInit
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
                } ?: emptyList()

                if (articleItems.isEmpty()) {
                    articleOldItems.add(ArticleItem("", "", "", "", null))
                    Toast.makeText(context, "불러올 기사가 없습니다.", Toast.LENGTH_SHORT).show()
                    isDone = true
                }

                articleOldItems.addAll(articleItems)


                Log.d("검색", "$pageInit, $articleOldItems")

                withContext(Dispatchers.Main) {

                    if (articleItems.isEmpty()) {
                        Toast.makeText(context, "불러올 기사가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    binding.rvKeywordArticle.adapter = NewsRecyclerViewAdapter(articleOldItems)
                    binding.rvKeywordArticle.adapter?.notifyDataSetChanged()
                    (binding.rvKeywordArticle.layoutManager as LinearLayoutManager).scrollToPosition(
                        savedScrollPosition
                    )
                }
            }
            isLoading = false // 로딩 상태를 다시 false로 설정
        }
    }
}