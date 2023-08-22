package com.example.cherry_pick_android.presentation.ui.newsSearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.ArticleItem
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchCommendService
import com.example.cherry_pick_android.databinding.FragmentSearchListBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SearchListFragment : Fragment() {
    private var _binding: FragmentSearchListBinding? = null
    private val binding get() = _binding!!
    private var cond = ""
    private var sort = ""
    private var isDone = false
    private var pageInit: Int = 0
    private var isLoading = false
    private lateinit var mRecyclerView: RecyclerView

    private var articleOldItems = mutableListOf<ArticleItem>()
    private var savedScrollPosition: Int = 0

    @Inject
    lateinit var articleService: ArticleSearchCommendService

    companion object {
        const val TAG = "ArticleSearchFragment"
        fun newInstance(): SearchListFragment = SearchListFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchListBinding.inflate(inflater, container, false)
        getArticleList()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScrollListener()
        binding.ibtnSortingMenu.setOnClickListener { showSortingMenu(it) }

    }


    private fun getArticleList() {
        // API 통신
        lifecycleScope.launch {
            val newsSearchActivity = activity as? NewsSearchActivity

            // NewsSearchActivity 인스턴스의 binding.etSearch.text 가져오기
            cond = newsSearchActivity?.getNowText().toString().trim()
            var sort = binding.tvSorting.text

            when (sort) {
                "인기순" -> sort = "like"
                "오름차순" -> sort = "asc"
                "내림차순" -> sort = "desc"
            }

            // trim으로 공백 제거
            val response = articleService.getArticleCommend(cond, sort.toString(), pageInit)
            val statusCode = response.body()?.statusCode

            withContext(Dispatchers.Main) {
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
                    } ?: emptyList()
                    articleOldItems = articleItems.toMutableList()
                    if (articleOldItems.size < 10) {
                        isDone = true
                    }
                    binding.rvSearchNewsList.adapter =
                        NewsRecyclerViewAdapter(articleItems.toMutableList())
                    binding.tvSearchCount.text = articleItems?.size.toString()
                } else {
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show()
                }
                binding.lottieDotLoading.visibility = View.GONE

            }
        }
    }

    // 메뉴
    private fun showSortingMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_article_sort, popupMenu.menu)

        // 메뉴 아이템 클릭 처리
        popupMenu.setOnMenuItemClickListener { item ->
            pageInit = 0

            when (item.itemId) {
                R.id.menu_sort_asc -> sort = getString(R.string.sort_article_asc)
                R.id.menu_sort_desc -> sort = getString(R.string.sort_article_desc)
                R.id.menu_sort_like -> sort = getString(R.string.sort_article_like)
            }
            binding.tvSorting.text = sort
            getArticleList()
            true
        }


        popupMenu.show()
    }


    private fun initScrollListener() {
        binding.rvSearchNewsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && !isDone && lastVisibleItemPosition == totalItemCount - 1) {
                    moreArticles(cond)
                    isLoading = true
                }
            }
        })
    }


    fun moreArticles(cond: String) {
        if (isLoading) return // 이미 로딩 중이라면 중복 호출 방지

        isLoading = true // 로딩 상태를 true로 설정

        mRecyclerView = binding.rvSearchNewsList

        // 페이지 번호를 증가시키고 새로운 기사를 로드
        pageInit++
        binding.lottieDotLoading.visibility = View.VISIBLE


        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // 임의의 딜레이 추가
            // 이전 스크롤 위치 저장
            binding.lottieDotLoading.visibility = View.GONE

            savedScrollPosition =
                (binding.rvSearchNewsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            lifecycleScope.launch {
                val response = articleService.getArticleCommend(
                    sortType = when (binding.tvSorting.text) {
                        "인기순" -> "like"
                        "오름차순" -> "asc"
                        "내림차순" -> "desc"
                        else -> ""
                    },
                    cond = cond.trim(),
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
                articleOldItems.addAll(articleItems)
                Log.d("검색", "$pageInit, $articleOldItems")

                withContext(Dispatchers.Main) {

                    if (articleItems.isEmpty()) {
                        Toast.makeText(context, "불러올 기사가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    binding.tvSearchCount.text = articleOldItems?.size.toString()
                    binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articleOldItems)
                    binding.rvSearchNewsList.adapter?.notifyDataSetChanged()
                    (binding.rvSearchNewsList.layoutManager as LinearLayoutManager).scrollToPosition(
                        savedScrollPosition
                    )
                }
            }
            isLoading = false // 로딩 상태를 다시 false로 설정
        }
    }
}

