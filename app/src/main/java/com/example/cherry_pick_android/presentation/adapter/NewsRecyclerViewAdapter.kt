package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemNewsBinding
import com.example.cherry_pick_android.data.data.News
import com.example.cherry_pick_android.presentation.ui.article.ArticleActivity

class NewsRecyclerViewAdapter(private val newsDataSet: List<News>):
    RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    // 뷰 유형에 대한 참조 클래스
    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setNewsItem(newsDataSet: String) {
            binding.tvNewsTitle.text = newsDataSet
        }
        // 아이템 클릭 이벤트 설정
        init {
            binding.root.setOnClickListener { view ->
                val context = view.context
                val intent = Intent(context, ArticleActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    // 아이템 레이아웃 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    // 호출한 내용으로 bind
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setNewsItem(newsDataSet[position].title)
    }

    // 데이터 크기 반환
    override fun getItemCount() = newsDataSet.size

}