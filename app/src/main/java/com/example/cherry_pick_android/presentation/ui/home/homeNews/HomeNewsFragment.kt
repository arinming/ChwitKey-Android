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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchCommendService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.example.cherry_pick_android.presentation.adapter.ArticleAdapter
import com.example.cherry_pick_android.presentation.adapter.ArticleItem
import com.example.cherry_pick_android.presentation.adapter.KeywordAdapter
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity
import com.example.cherry_pick_android.presentation.viewmodel.article.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeNewsFragment : Fragment(R.layout.fragment_home_news) {
    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var articleService: ArticleSearchCommendService
    @Inject
    lateinit var userInfoService: UserInfoService
    @Inject
    lateinit var userDataRepository: UserDataRepository

    lateinit var recyclerViewAdapter: ArticleAdapter
    private val viewModel: ArticleViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNewsBinding.inflate(inflater, container, false)

        industryLoad()

        getArticleList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewsList()
        goToNewsSearch()

        // 유저 정보 갱신
        lifecycleScope.launch {
            userDataRepository.getUserData()
        }


        binding.ibtnSortingMenu.setOnClickListener { showSortingMenu(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getArticleList() {
        // API 통신
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val industry = binding.ibtnKeyWord1.text
                var sort = binding.tvSorting.text
                when (sort) {
                    "인기순" -> sort = "like"
                    "오름차순" -> sort = "asc"
                    "내림차순" -> sort = "desc"
                }
                val response = articleService.getArticleCommend(industry.toString(), sort.toString(), Pageable)
                val statusCode = response.body()?.statusCode
                if (statusCode == 200) {
                    val articleItems = response.body()?.data?.content?.map { content ->
                        val imageUrl = if (content.articlePhoto.isNotEmpty()) content.articlePhoto[0].articleImgUrl else "" // 기사 사진이 없으면 빈 문자열로 처리
                        ArticleItem(content.title, content.publisher, content.uploadedAt, imageUrl)
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

    private fun initNewsList() {
        recyclerViewAdapter = ArticleAdapter()
        binding.rvNewsList.adapter = recyclerViewAdapter
    }



    // 메뉴
    private fun showSortingMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_article_sort, popupMenu.menu)

        // 메뉴 아이템 클릭 처리
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_sort_asc -> binding.tvSorting.text = getString(R.string.sort_article_asc)
                R.id.menu_sort_desc -> binding.tvSorting.text = getString(R.string.sort_article_desc)
                R.id.menu_sort_like -> binding.tvSorting.text = getString(R.string.sort_article_like)
            }
            true
        }

        getArticleList()

        popupMenu.show()
    }


    private fun mapperToJob(value: String): String{
        return when(value){
            "steel" -> "철강" "Petroleum/Chemical" -> "석유·화학" "oilrefining" -> "정유" "secondarybattery" -> "2차 전지"
            "Semiconductor" -> "반도체" "Display" -> "디스플레이" "Mobile" -> "휴대폰" "It" -> "IT"
            "car" -> "자동차" "Shipbuilding" -> "조선" "Shipping" -> "해운" "FnB" -> "F&B"
            "RetailDistribution" -> "소매유통" "Construction" -> "건설" "HotelTravel" -> "호텔·여행·항공" "FiberClothing" -> "섬유·의류"
            else -> ""
        }
    }

    private fun industryLoad(){


        lifecycleScope.launch {
            val response = userInfoService.getUserInfo().body()
            val statusCode = response?.statusCode
            val industryResponse =
                "${mapperToJob(response?.data?.industryKeyword1.toString())}, " +
                        "${mapperToJob(response?.data?.industryKeyword2.toString())}, " +
                        mapperToJob(response?.data?.industryKeyword3.toString())
            withContext(Dispatchers.Main){
                if(statusCode == 200){
                    Log.d("직군", industryResponse)
                }else{
                }
            }
        }
    }

}