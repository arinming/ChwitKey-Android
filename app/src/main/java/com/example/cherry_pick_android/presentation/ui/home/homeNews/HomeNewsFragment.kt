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
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.presentation.adapter.ArticleAdapter
import com.example.cherry_pick_android.presentation.adapter.ArticleItem
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

    lateinit var recyclerViewAdapter: ArticleAdapter
    private val viewModel: ArticleViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNewsBinding.inflate(inflater, container, false)


        getArticleList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewsList()
        goToNewsSearch()

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
                if (sort == "인기순") {
                    sort = "like"
                } else if (sort == "오름차순") {
                    sort = "asc"
                } else if (sort == "내림차순") {
                    sort = "desc"
                }
                val response = articleService.getArticleCommend(industry.toString(), sort.toString(), Pageable)
                val statusCode = response.body()?.statusCode
                if (statusCode == 200) {
                    val articleItems = response.body()?.data?.content?.map { content ->
                        val imageUrl = if (content.articlePhoto.isNotEmpty()) content.articlePhoto[0].articleImgUrl else "" // 기사 사진이 없으면 빈 문자열로 처리
                        ArticleItem(content.title, content.publisher, content.uploadedAt, imageUrl, content.articleId)
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

}