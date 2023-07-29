package com.example.cherry_pick_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemKeywordBtnBinding
import com.example.cherry_pick_android.presentation.ui.keyword.Keyword

class KeywordAdapter(private val keywords: List<Keyword>): RecyclerView.Adapter<KeywordAdapter.KeywordHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordHolder {
        val binding = ItemKeywordBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordHolder(binding)
    }

    override fun getItemCount() = keywords.size

    override fun onBindViewHolder(holder: KeywordHolder, position: Int) {
        holder.setItem(keywords[position].keyword)
    }

    class KeywordHolder(val binding: ItemKeywordBtnBinding): RecyclerView.ViewHolder(binding.root){
        fun setItem(keyword: String){
            binding.btnKeyword.text =  keyword
        }
    }
}