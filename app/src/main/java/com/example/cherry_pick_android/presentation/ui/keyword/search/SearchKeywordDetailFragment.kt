package com.example.cherry_pick_android.presentation.ui.keyword.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.databinding.FragmentSearchKeywordDetailBinding

class SearchKeywordDetailFragment: Fragment() {
    private val binding: FragmentSearchKeywordDetailBinding by lazy {
        FragmentSearchKeywordDetailBinding.inflate(layoutInflater)
    }
    companion object{
        const val TAG = "searchKeywordDetailFragment"
        fun newInstance(): SearchKeywordDetailFragment = SearchKeywordDetailFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}