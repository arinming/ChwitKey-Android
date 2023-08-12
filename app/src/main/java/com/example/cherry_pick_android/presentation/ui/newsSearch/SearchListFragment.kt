package com.example.cherry_pick_android.presentation.ui.newsSearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.data.data.Article
import com.example.cherry_pick_android.databinding.FragmentSearchListBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchListFragment : Fragment() {
    private var _binding: FragmentSearchListBinding? = null
    private val binding get() = _binding!!


    private var searchListFragment: SearchListFragment? = null

    companion object {
        const val TAG = "ArticleSearchFragment"
        fun newInstance(): SearchListFragment = SearchListFragment()
    }

    private val articles = listOf(
        Article("1", "뉴스1", "회사1", "9분"),
        Article("2", "뉴스2", "회사2", "19분"),
        Article("3", "뉴스3", "회사3", "29분"),
        Article("4", "뉴스4", "회사4", "39분"),
        Article("5", "뉴스5", "회사5", "49분"),
        Article("6", "뉴스6", "회사6", "59분"),
        Article("7", "뉴스7", "회사7", "1시간"),
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewsList()

    }

    private fun initNewsList() {
        binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articles)
        binding.tvSearchCount.text = articles.size.toString()
    }


}

