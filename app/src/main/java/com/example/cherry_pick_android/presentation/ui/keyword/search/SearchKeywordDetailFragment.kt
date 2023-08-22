package com.example.cherry_pick_android.presentation.ui.keyword.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.ArticleItem
import com.example.cherry_pick_android.data.model.KeywordEntity
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchKeywordService
import com.example.cherry_pick_android.databinding.FragmentSearchKeywordDetailBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.adapter.SearchKeywordAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.dialog.KeywordDialog
import com.example.cherry_pick_android.presentation.viewmodel.keyword.SearchKeywordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SearchKeywordDetailFragment : Fragment() {
    private val binding: FragmentSearchKeywordDetailBinding by lazy {
        FragmentSearchKeywordDetailBinding.inflate(layoutInflater)
    }
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels() // 뷰모델 초기화 불필요 (Hilt)
    private lateinit var searchKeywordAdapter: SearchKeywordAdapter
    private var keyword = ""
    private var isKeyword = false
    private var isDone = false
    private var pageInit: Int = 0
    private var isLoading = false
    private lateinit var nowKeyword : List<KeywordEntity>
    private lateinit var mRecyclerView: RecyclerView

    private var articleOldItems = mutableListOf<ArticleItem>()
    private var savedScrollPosition: Int = 0

    @Inject
    lateinit var articleService: ArticleSearchKeywordService

    companion object {
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
        getArticleList()
        getKeyword()
        initScrollListener()

        // DB 데이터 로드 및 개수 초기화
        searchKeywordViewModel.loadKeyword().observe(viewLifecycleOwner) {
            nowKeyword = searchKeywordViewModel.loadKeyword().value!!
        }
    }


    private fun getKeyword() {
        // 키워드 등록 버튼 이벤트
        binding.btnComplete.setOnClickListener {
            val searchKeywordFragment = parentFragment as? SearchKeywordFragment
            val keyword = searchKeywordFragment?.getNowText().toString().trim()

            val isKeywordNew = searchKeywordViewModel.checkKeyword(keyword) // 중복 키워드 검사
            val isKeywordCnt = searchKeywordViewModel.checkKeywordCnt() // 키워드 개수 검사
            Log.d("키워드 검사", "${searchKeywordViewModel.loadKeyword().value.toString()}")
            if (keyword.isNotEmpty() && isKeywordCnt) {
                if (isKeywordNew) {
                    searchKeywordViewModel.addKeyword(keyword)
                    hideKeyboard()
                    KeywordDialog().show(parentFragmentManager, "keyword_dialog")
                    showFragment(SearchKeywordFragment.newInstance(), SearchKeywordFragment.TAG)
                } else {
                    Snackbar.make(binding.root, "이미 존재하는 키워드입니다", Snackbar.LENGTH_SHORT).show()
                }
            } else if (keyword.isEmpty()) {
                Snackbar.make(binding.root, "키워드를 입력하지 않았습니다", Snackbar.LENGTH_SHORT).show()
            } else if (!isKeywordCnt) {
                Snackbar.make(binding.root, "키워드 최대 개수를 초과했습니다", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    fun getArticleList() {
        // API 통신
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val searchKeywordFragment = parentFragment as? SearchKeywordFragment
                keyword = searchKeywordFragment?.getNowText().toString().trim()

                if (::articleService.isInitialized) {
                    val response = articleService.getArticleKeyword(
                        sortType = "desc",
                        keyword = keyword,
                        page = pageInit
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
                        articleOldItems = articleItems?.toMutableList()!!
                        if (articleOldItems.size < 10) {
                            isDone = true
                        }
                        Log.d("기사", articleItems.toString())
                        binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articleItems)
                    } else {
                        Snackbar.make(binding.root, "오류가 발생했습니다. (Code: $statusCode)", Snackbar.LENGTH_SHORT).show()
                    }
                    binding.lottieDotLoading.visibility = View.GONE
                }
            }
        }
    }

    // 키패드 제거 함수
    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
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
                .add(R.id.fv_home, fragment, tag)
        transaction.addToBackStack(tag).commitAllowingStateLoss()
    }

    fun loadArticlesByKeyword(keyword: String) {
        savedScrollPosition = 0
        pageInit = 0
        isDone = false

        lifecycleScope.launch {
            val response = articleService.getArticleKeyword(
                sortType = "desc",
                keyword = keyword,
                page = 0
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
            articleItems?.toMutableList()?.let { articleOldItems.addAll(it) }
            Log.d("리스트", "${articleOldItems.size} ,${articleOldItems.toString()}")
            if (articleOldItems.size < 10) {
                isDone = true
            }

            withContext(Dispatchers.Main) {


                binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articleItems)
                (binding.rvSearchNewsList.layoutManager as LinearLayoutManager).scrollToPosition(
                    savedScrollPosition
                )
                isKeyword = false

            }
        }
    }

    private fun initScrollListener() {
        binding.rvSearchNewsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && !isDone && lastVisibleItemPosition == totalItemCount - 1) {
                    savedScrollPosition =
                        (binding.rvSearchNewsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    moreArticles(keyword)
                    isLoading = false
                }
            }
        })
    }


    fun moreArticles(keyword: String) {
        if (isLoading) return // 이미 로딩 중이라면 중복 호출 방지
        if (isKeyword) return

        mRecyclerView = binding.rvSearchNewsList

        // 페이지 번호를 증가시키고 새로운 기사를 로드
        pageInit++
        binding.lottieDotLoading.visibility = View.VISIBLE

        lifecycleScope.launch {
            delay(1000) // 임의의 딜레이 추가
            // 이전 스크롤 위치 저장
            binding.lottieDotLoading.visibility = View.GONE

            val response = articleService.getArticleKeyword(
                sortType = "desc",
                keyword = keyword.trim(),
                page = pageInit
            )

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
                Snackbar.make(binding.root, "불러올 기사가 없습니다.", Snackbar.LENGTH_SHORT).show()
                isDone = true
            }

            articleOldItems.addAll(articleItems)
            Log.d("추가 리스트", "${articleOldItems.size} ,${articleOldItems.toString()}")

            withContext(Dispatchers.Main) {
                binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articleOldItems)
                binding.rvSearchNewsList.adapter?.notifyDataSetChanged()
                (binding.rvSearchNewsList.layoutManager as LinearLayoutManager).scrollToPosition(
                    savedScrollPosition
                )
            }
        }
    }


    fun clearPage(text: String) {
        articleOldItems.clear()
        pageInit = 0
        isDone = false

        loadArticlesByKeyword(text)
    }
}