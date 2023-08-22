package com.chwitkey.cherry_pick_android.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chwitkey.cherry_pick_android.data.model.KeywordEntity
import com.chwitkey.cherry_pick_android.databinding.ItemKeywordCompleteBtnBinding
import com.chwitkey.cherry_pick_android.presentation.ui.keyword.DeleteListener

class SearchKeywordAdapter(private val deleteListener: DeleteListener): RecyclerView.Adapter<SearchKeywordAdapter.SearchKeywordHolder>() {
    private val items = ArrayList<KeywordEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeywordHolder {
        val binding = ItemKeywordCompleteBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchKeywordHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SearchKeywordHolder, position: Int) {
        holder.setItem(items[position].keyword)
        holder.binding.btnClearKeyword.setOnClickListener {
            Log.d("SearchKeywordAdaper", "clearBtnClick")
            deleteListener.onDeleteClick(items[position].keyword)
        }
    }

    inner class SearchKeywordHolder(val binding: ItemKeywordCompleteBtnBinding): RecyclerView.ViewHolder(binding.root){
        fun setItem(keyword: String){
            binding.btnKeyword.text = keyword
        }
    }

    // 리스트 설정
    fun setList(list: List<KeywordEntity>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}