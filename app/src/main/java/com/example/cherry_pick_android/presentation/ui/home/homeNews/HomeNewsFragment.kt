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
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.ArticleItem
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchCommendService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.example.cherry_pick_android.presentation.adapter.IndustryAdapter
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.keyword.AdapterInteractionListener
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeNewsFragment : Fragment(R.layout.fragment_home_news), AdapterInteractionListener {
    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!

    private var industryInit: String = ""
    private var isMenuOpen = false

    @Inject
    lateinit var articleService: ArticleSearchCommendService

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

    private fun getArticleList(sort: String) {
        // API 통신
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val response = articleService.getArticleCommend(
                    cond = industryInit,
                    sortType = sort,
                    pageable = Pageable
                )
                Log.d("직군 정렬", "$industryInit, $sort")

                val statusCode = response.body()?.statusCode
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
                    }
                    binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articleItems)
                } else {
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show()
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


    // 메뉴
    private fun showSortingMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_article_sort, popupMenu.menu)

        // 메뉴 아이템 클릭 처리
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_sort_asc -> {
                    binding.tvSorting.text = getString(R.string.sort_article_asc)
                    getArticleList("asc")
                }
                R.id.menu_sort_desc -> {
                    binding.tvSorting.text = getString(R.string.sort_article_desc)
                    getArticleList("desc")
                }
                R.id.menu_sort_like -> {
                    binding.tvSorting.text = getString(R.string.sort_article_like)
                    getArticleList("like")
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
                    Log.d("현재 직군", industryInit)
                    loadArticlesByIndustry(industry1)

                    binding.rvIndustry.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    binding.rvIndustry.adapter = IndustryAdapter(industries, this@HomeNewsFragment)
                    Log.d("직군", "$industry1, $industry2, $industry3")
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // 버튼 클릭 시 호출되는 함수
    private fun onIndustryButtonClick(industry: String) {
        industryInit = industry // 클릭한 버튼의 텍스트를 저장
        Log.d("직군 함수 호출", industryInit) // 클릭한 버튼의 텍스트 로그로 출력
    }

    // 버튼 클릭시 뉴스 리스트 갱신
    private fun loadArticlesByIndustry(keyword: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val response = articleService.getArticleCommend(
                    sortType = when (binding.tvSorting.text) {
                        "인기순" -> "like"
                        "오름차순" -> "asc"
                        "내림차순" -> "desc"
                        else -> ""
                    },
                    cond = keyword,
                    pageable = Pageable
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
                }
                binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articleItems)
            }
        }
    }

    override fun onButtonSelected(button: String) {
        selectedIndustry = button
        industryInit = button
        loadArticlesByIndustry(button)
    }
}