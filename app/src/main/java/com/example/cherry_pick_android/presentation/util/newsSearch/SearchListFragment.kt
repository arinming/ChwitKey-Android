package com.example.cherry_pick_android.presentation.util.newsSearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.ArticleItem
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.service.article.ArticleSearchCommendService
import com.example.cherry_pick_android.databinding.FragmentSearchListBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SearchListFragment : Fragment() {
    private var _binding: FragmentSearchListBinding? = null
    private val binding get() = _binding!!

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


        binding.ibtnSortingMenu.setOnClickListener { showSortingMenu(it) }

    }


    private fun getArticleList() {
        // API 통신
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val newsSearchActivity = activity as? NewsSearchActivity

                // NewsSearchActivity 인스턴스의 binding.etSearch.text 가져오기
                val cond = newsSearchActivity?.getNowText()
                var sort = binding.tvSorting.text

                when (sort) {
                    "인기순" -> sort = "like"
                    "오름차순" -> sort = "asc"
                    "내림차순" -> sort = "desc"
                }

                // trim으로 공백 제거
                val response = articleService.getArticleCommend(cond.toString().trim(), sort.toString(), Pageable(1, 10, ""))


                val statusCode = response.body()?.statusCode
                if (statusCode == 200) {
                    val articleItems = response.body()?.data?.content?.map { content ->
                        val imageUrl = if (content.articlePhoto.isNotEmpty()) content.articlePhoto[0].articleImgUrl else "" // 기사 사진이 없으면 빈 문자열로 처리
                        ArticleItem(content.title, content.publisher, content.uploadedAt, imageUrl, content.articleId)
                    }?.toMutableList()
                    binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articleItems)
                    binding.tvSearchCount.text = articleItems?.size.toString()
                } else {
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show()
                }
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
                R.id.menu_sort_asc -> binding.tvSorting.text = getString(R.string.sort_article_asc)
                R.id.menu_sort_desc -> binding.tvSorting.text = getString(R.string.sort_article_desc)
                R.id.menu_sort_like -> binding.tvSorting.text = getString(R.string.sort_article_like)
            }
            getArticleList()
            true
        }


        popupMenu.show()
    }

}

