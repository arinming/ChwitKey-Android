package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemNewsBinding
import com.example.cherry_pick_android.data.data.Article
import com.example.cherry_pick_android.presentation.ui.article.ArticleActivity

class NewsRecyclerViewAdapter(private val articleDataSet: List<Article>):
    RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    // 뷰 유형에 대한 참조 클래스
    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvNewsTitle
        val company = binding.tvNewsCompany
        val time = binding.tvNewsTime

        // 아이템 클릭 이벤트 설정
        init {
            binding.root.setOnClickListener { view ->
                val context = view.context
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("제목", binding.tvNewsTitle.text)
                intent.putExtra("회사", binding.tvNewsCompany.text)
                intent.putExtra("시간", binding.tvNewsTime.text)
                ContextCompat.startActivity(context, intent, null)
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
        viewHolder.title.text = articleDataSet[position].title
        viewHolder.company.text = articleDataSet[position].company
        viewHolder.time.text = articleDataSet[position].time
    }

    // 데이터 크기 반환
    override fun getItemCount() = articleDataSet.size

}