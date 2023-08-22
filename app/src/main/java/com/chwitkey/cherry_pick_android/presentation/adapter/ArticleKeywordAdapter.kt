package com.chwitkey.cherry_pick_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chwitkey.cherry_pick_android.data.data.Keyword
import com.chwitkey.cherry_pick_android.databinding.ItemKeywordBtnBinding
import com.chwitkey.cherry_pick_android.presentation.ui.keyword.AddListener

class ArticleKeywordAdapter(
    private val keywords: List<Keyword>,
    private val addListener: AddListener? = null
) : RecyclerView.Adapter<ArticleKeywordAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemKeywordBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItem(keyword: String) {
            binding.btnKeyword.text = keyword
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemKeywordBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = keywords.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(keywords[position].keyword)
        holder.binding.btnKeyword.setOnClickListener {
            addListener?.onAddClick(keywords[position].keyword)
        }
    }

}