package com.example.cherry_pick_android.presentation.ui.scrap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.data.data.Article
import com.example.cherry_pick_android.databinding.FragmentScrapTrueBinding
import com.example.cherry_pick_android.presentation.adapter.ScrapAdapter

class ScrapTrueFragment : Fragment() {
    private var _binding: FragmentScrapTrueBinding? = null
    private val binding get() = _binding!!

    private val scrapNews = mutableListOf(
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
        _binding = FragmentScrapTrueBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScrapList()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initScrapList() {
        binding.rvScrapNewsList.adapter = ScrapAdapter(scrapNews)
        binding.tvScrapCount.text = scrapNews.size.toString()
    }
}