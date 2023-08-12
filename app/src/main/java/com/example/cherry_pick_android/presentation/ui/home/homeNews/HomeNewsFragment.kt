package com.example.cherry_pick_android.presentation.ui.home.homeNews

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.Article
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.presentation.adapter.ArticleAdapter
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity
import com.example.cherry_pick_android.presentation.viewmodel.article.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeNewsFragment : Fragment(R.layout.fragment_home_news) {
    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!

    private val articlesList = ArrayList<Article>()
    lateinit var recyclerViewAdapter: ArticleAdapter
    private val viewModel: ArticleViewModel by viewModels()


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
        liveNewsList()
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
        recyclerViewAdapter = ArticleAdapter()
        binding.rvNewsList.adapter = recyclerViewAdapter
    }

    private fun liveNewsList() {
        viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                recyclerViewAdapter.setListData(it)
                recyclerViewAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show()
            }
        })

        // loadListOfData 함수를 호출하는 코루틴 시작
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadListOfData()
        }
    }
}