package com.example.cherry_pick_android.presentation.ui.searchList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.databinding.ActivitySearchListBinding
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.data.data.Article

class SearchListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchListBinding

    private val articles = listOf(
        Article("1", "뉴스1"), Article("2","뉴스2"), Article("3","뉴스3"), Article("4","뉴스4"),
        Article("5","뉴스5"), Article("6","뉴스6"), Article("7","뉴스7"), Article("8","뉴스8"),
        Article("9","뉴스9"), Article("9","뉴스10")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchListBinding.inflate(layoutInflater)
        val view = binding.root

        initNewsList()
        goToBack()

        setContentView(view)
    }

    private fun initNewsList() {
        binding.rvSearchNewsList.adapter = NewsRecyclerViewAdapter(articles)
        binding.tvSearchCount.text = articles.size.toString()
    }

    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }
}