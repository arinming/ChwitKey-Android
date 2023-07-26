package com.example.cherry_pick_android.presentation.ui.home.homeNews

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.FragmentHomeNewsBinding
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity

class HomeNewsFragment: Fragment(R.layout.fragment_home_news) {
    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        goToNewsSearch()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 검색창 누르면 NewsSearch 액티비티로 이동
    private fun goToNewsSearch() {
        binding.btnHomeSearchBackground.setOnClickListener {
            activity?.let {
                val intent = Intent(it, NewsSearchActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}