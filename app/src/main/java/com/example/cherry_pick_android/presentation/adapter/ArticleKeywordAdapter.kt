package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.data.data.Keyword
import com.example.cherry_pick_android.databinding.ItemKeywordBtnBinding

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
    }

}