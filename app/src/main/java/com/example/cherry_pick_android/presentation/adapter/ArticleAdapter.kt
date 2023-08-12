package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.data.remote.response.ArticleCommendResponse
import com.example.cherry_pick_android.databinding.ItemNewsBinding
import com.example.cherry_pick_android.presentation.ui.article.ArticleActivity

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    private var listData: List<ArticleCommendResponse.Data>? = null

    fun setListData(listData: List<ArticleCommendResponse.Data>?) {
        this.listData = listData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleAdapter.ViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }




    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        holder
    }

    class ViewHolder(val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
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

    override fun getItemCount(): Int = listData?.size!!

}