package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cherry_pick_android.databinding.ItemNewsBinding
import com.example.cherry_pick_android.presentation.ui.article.ArticleActivity

data class ArticleItem(
    val title: String,
    val company: String,
    val time: String,
    val picture: String,
    val id: Int
)

class NewsRecyclerViewAdapter(private val articleDataSet: List<ArticleItem>?) :
    RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    // 뷰 유형에 대한 참조 클래스
    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvNewsTitle
        val company = binding.tvNewsCompany
        val time = binding.tvNewsTime
        val image = binding.ivNewsImagePreview
        var id: Int? = 0


        // 아이템 클릭 이벤트 설정
        init {
            binding.root.setOnClickListener { view ->
                val context = view.context
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("제목", binding.tvNewsTitle.text)
                intent.putExtra("회사", binding.tvNewsCompany.text)
                intent.putExtra("시간", binding.tvNewsTime.text)
                intent.putExtra("id", id)
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
        val articleItem = articleDataSet?.get(position)
        viewHolder.title.text = articleItem?.title
        viewHolder.company.text = articleItem?.company
        viewHolder.time.text = articleItem?.time
        viewHolder.id = articleItem?.id


        Glide.with(viewHolder.itemView.context)
            .load(articleItem?.picture)
            .into(viewHolder.image)
    }

    // 데이터 크기 반환
    override fun getItemCount() = articleDataSet?.size ?: 0

}