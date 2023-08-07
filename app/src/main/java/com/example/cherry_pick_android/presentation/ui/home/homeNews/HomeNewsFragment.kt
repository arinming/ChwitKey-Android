package com.example.cherry_pick_android.presentation.ui.home.homeNews

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.Article
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity
import com.example.cherry_pick_android.presentation.viewmodel.article.ArticleViewModel

class HomeNewsFragment : Fragment(R.layout.fragment_home_news) {
    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!

    private val articles = listOf(
        Article("1", "뉴스1"), Article("2", "뉴스2"), Article("3", "뉴스3"), Article("4", "뉴스4"),
        Article("5", "뉴스5"), Article("6", "뉴스6"), Article("7", "뉴스7"), Article("8", "뉴스8"),
        Article("9", "뉴스9"), Article("9", "뉴스10")
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeNewsBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewsList()
        goToNewsSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articles)
    }

    private fun liveNewsList() {
        val viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        viewModel.getAllArticle()

        viewModel.result.observe(viewLifecycleOwner, Observer {
            binding.rvNewsList.adapter = NewsRecyclerViewAdapter(articles)
        })
    }
}