package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.data.data.Keyword
import com.example.cherry_pick_android.databinding.ItemKeywordBtnBinding
import com.example.cherry_pick_android.presentation.ui.searchList.SearchListActivity

class ArticleKeywordAdapter(
    private val keywords: List<Keyword>
) : RecyclerView.Adapter<ArticleKeywordAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemKeywordBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val keyword = binding.btnKeyword
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemKeywordBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = keywords.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keyword.text = keywords[position].keyword
        holder.keyword.setOnClickListener {
            val intent = Intent(holder.keyword.context, SearchListActivity::class.java)
            intent.putExtra("키워드", holder.keyword.text)
            ContextCompat.startActivity(holder.keyword.context, intent, null)
        }
    }

}