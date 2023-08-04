package com.example.cherry_pick_android.presentation.ui.scrap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.data.data.ScrapNews
import com.example.cherry_pick_android.databinding.FragmentScrapTrueBinding
import com.example.cherry_pick_android.presentation.adapter.ScrapAdapter

class ScrapTrueFragment : Fragment() {
    private var _binding: FragmentScrapTrueBinding? = null
    private val binding get() = _binding!!

    private val scrapNews = mutableListOf(
        ScrapNews("뉴스1"), ScrapNews("뉴스2"), ScrapNews("뉴스3"), ScrapNews("뉴스4"),
        ScrapNews("뉴스5"), ScrapNews("뉴스6"), ScrapNews("뉴스7"), ScrapNews("뉴스8"),
        ScrapNews("뉴스9"), ScrapNews("뉴스10"), ScrapNews("뉴스11"), ScrapNews("뉴스12")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrapTrueBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
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
    }
}