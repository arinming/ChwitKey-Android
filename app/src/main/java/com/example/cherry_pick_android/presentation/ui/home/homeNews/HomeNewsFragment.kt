package com.example.cherry_pick_android.presentation.ui.home.homeNews

import android.content.Intent
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
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchIndustryService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.example.cherry_pick_android.presentation.adapter.IndustryAdapter
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.AdapterInteractionListener
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeNewsFragment : Fragment(R.layout.fragment_home_news), AdapterInteractionListener {
    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!

    private var industryInit: String = ""
    private var sort = ""
    private var pageInit: Int = 0
    private var isLoading = false
    private var isDone = false
    private lateinit var mRecyclerView: RecyclerView

    private var articleOldItems = mutableListOf<ArticleItem>()
    private var savedScrollPosition: Int = 0


    @Inject
    lateinit var articleService: ArticleSearchIndustryService

    @Inject
    lateinit var userInfoService: UserInfoService

    @Inject
    lateinit var userDataRepository: UserDataRepository

    private lateinit var selectedIndustry: String


    companion object {
        const val TAG = "HomeNewsFragmnet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        industryLoad()
        goToNewsSearch()
        initScrollListener()


        // 유저 정보 갱신
        lifecycleScope.launch {
            userDataRepository.getUserData()
        }

        binding.ibtnSortingMenu.setOnClickListener {
            showSortingMenu(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getArticleList(sort: String, industry: String) {
        // API 통신
        lifecycleScope.launch {
            var nowIndustry = mapperToIndustry(industry)

            val response = articleService.getArticleIndustry(
                industry = nowIndustry,
                sortType = sort,
                page = pageInit
            )

            val statusCode = response.body()?.statusCode
            withContext(Dispatchers.Main) {
                if (statusCode == 200) {
                    onIndustryButtonClick(industryInit)
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
                    }?: emptyList()
                    articleOldItems = articleItems.toMutableList()
                    binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articleOldItems)
                }
            }
        }
    }


    // 검색창 누르면 NewsSearch 액티비티로 이동
    private fun goToNewsSearch() {
        binding.ibtnHomeSearchBackground.setOnClickListener {
            activity?.let {
                val intent = Intent(it, NewsSearchActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    // 한글 -> 영문 매핑
    private fun mapperToIndustry(value: String): String {
        return when (value) {
            "철강" -> "steel"
            "석유·화학" -> "Petroleum/Chemical"
            "정유" -> "oilrefining"
            "2차 전지" -> "seconarybattery"
            "반도체" -> "Semiconductor"
            "디스플레이" -> "Display"
            "휴대폰" -> "Mobile"
            "IT" -> "It"
            "자동차" -> "car"
            "조선" -> "Shipbuilding"
            "해운" -> "Shipping"
            "F&B" -> "FnB"
            "소매유통" -> "RetailDistribution"
            "건설" -> "Construction"
            "호텔·여행·항공" -> "HotelTravel"
            "섬유·의류" -> "FiberClothing"
            else -> ""
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
                R.id.menu_sort_asc -> {
                    sort = "asc"
                    binding.tvSorting.text = getString(R.string.sort_article_asc)
                    getArticleList(sort, industryInit)
                    industryInit
                }

                R.id.menu_sort_desc -> {
                    sort = "desc"
                    binding.tvSorting.text = getString(R.string.sort_article_desc)
                    getArticleList(sort, industryInit)
                }

                R.id.menu_sort_like -> {
                    sort = "like"
                    binding.tvSorting.text = getString(R.string.sort_article_like)
                    getArticleList(sort, industryInit)
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun mapperToJob(value: String): String {
        return when (value) {
            "steel" -> "철강"
            "Petroleum/Chemical" -> "석유·화학"
            "oilrefining" -> "정유"
            "secondarybattery" -> "2차 전지"
            "Semiconductor" -> "반도체"
            "Display" -> "디스플레이"
            "Mobile" -> "휴대폰"
            "It" -> "IT"
            "car" -> "자동차"
            "Shipbuilding" -> "조선"
            "Shipping" -> "해운"
            "FnB" -> "F&B"
            "RetailDistribution" -> "소매유통"
            "Construction" -> "건설"
            "HotelTravel" -> "호텔·여행·항공"
            "FiberClothing" -> "섬유·의류"
            else -> ""
        }
    }


    private fun industryLoad() {
        lifecycleScope.launch {
            val response = userInfoService.getUserInfo().body()
            val statusCode = response?.statusCode
            val industry1 = mapperToJob(response?.data?.industryKeyword1.toString())
            val industry2 = mapperToJob(response?.data?.industryKeyword2.toString())
            val industry3 = mapperToJob(response?.data?.industryKeyword3.toString())

            val industries = mutableListOf<String>() // 사용자 직군을 저장할 리스트

            // 빈 값을 추가하지 않도록 조건 체크 후 추가
            if (industry1.isNotBlank()) {
                industries.add(industry1)
            }
            if (industry2.isNotBlank()) {
                industries.add(industry2)
            }
            if (industry3.isNotBlank()) {
                industries.add(industry3)
            }
            onIndustryButtonClick(industry1)


            withContext(Dispatchers.Main) {
                if (statusCode == 200) {
                    loadArticlesByIndustry(industry1)

                    binding.rvIndustry.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    binding.rvIndustry.adapter = IndustryAdapter(industries, this@HomeNewsFragment)
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // 버튼 클릭 시 호출되는 함수
    private fun onIndustryButtonClick(industry: String) {
        industryInit = industry // 클릭한 버튼의 텍스트를 저장
    }

    // 버튼 클릭시 뉴스 리스트 갱신
    private fun loadArticlesByIndustry(industry: String) {
        savedScrollPosition = (binding.rvNewsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        binding.lottieDotLoading.visibility = View.VISIBLE

        lifecycleScope.launch {

            var nowIndustry = mapperToIndustry(industry)
            val response = articleService.getArticleIndustry(
                sortType = when (binding.tvSorting.text) {
                    "인기순" -> "like"
                    "오름차순" -> "asc"
                    "내림차순" -> "desc"
                    else -> ""
                },
                industry = nowIndustry,
                page = 0
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

            withContext(Dispatchers.Main) {
                binding.lottieDotLoading.visibility = View.GONE
                binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articleOldItems)
                binding.rvNewsList.adapter?.notifyDataSetChanged()
                (binding.rvNewsList.layoutManager as LinearLayoutManager).scrollToPosition(savedScrollPosition)
            }
        }
    }



    private fun initScrollListener() {
        binding.rvNewsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && !isDone && lastVisibleItemPosition == totalItemCount - 1) {
                    moreArticles(industryInit)
                    isLoading = true
                }
            }
        })
    }


    fun moreArticles(industry: String) {
        if (isLoading) return // 이미 로딩 중이라면 중복 호출 방지

        isLoading = true // 로딩 상태를 true로 설정

        mRecyclerView = binding.rvNewsList

        // 페이지 번호를 증가시키고 새로운 기사를 로드
        pageInit++
        binding.lottieDotLoading.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // 임의의 딜레이 추가
            // 이전 스크롤 위치 저장
            binding.lottieDotLoading.visibility = View.GONE

            savedScrollPosition = (binding.rvNewsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            lifecycleScope.launch {

                var nowIndustry = mapperToIndustry(industry)
                val response = articleService.getArticleIndustry(
                    sortType = when (binding.tvSorting.text) {
                        "인기순" -> "like"
                        "오름차순" -> "asc"
                        "내림차순" -> "desc"
                        else -> ""
                    },
                    industry = nowIndustry,
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
                }?: emptyList()

                if (articleItems.isEmpty()) {
                    articleOldItems.add(ArticleItem("", "", "", "", null))
                    Toast.makeText(context, "불러올 기사가 없습니다.", Toast.LENGTH_SHORT).show()
                    isDone = true
                }

                articleOldItems.addAll(articleItems)
                Log.d("직군", "$pageInit, $response")

                withContext(Dispatchers.Main) {

                    binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articleOldItems)
                    binding.rvNewsList.adapter?.notifyDataSetChanged()
                    (binding.rvNewsList.layoutManager as LinearLayoutManager).scrollToPosition(savedScrollPosition)

                }
            }
            isLoading = false // 로딩 상태를 다시 false로 설정
        }
    }

    override fun onButtonSelected(button: String) {
        selectedIndustry = button
        if(industryInit != button){
            industryInit = button
            pageInit = 0
            articleOldItems.clear()
        }
        loadArticlesByIndustry(industryInit)
    }
}