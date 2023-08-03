package com.example.cherry_pick_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemSearchBinding
import com.example.cherry_pick_android.domain.model.SearchRecord

class SearchRecordAdapter(private val record: List<SearchRecord>): RecyclerView.Adapter<SearchRecordAdapter.ViewHolder>() {

    // 뷰 유형에 대한 참조 클래스
    class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setRecordItem(recodes: String) {
            binding.btnRecentSearchItem.text = recodes
        }
    }

    // 아이템 레이아웃 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )

        return ViewHolder(binding)
    }

    // 호출한 내용으로 bind
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setRecordItem(record[position].searchRecode)
    }

    // 데이터 크기 반환
    override fun getItemCount() = record.size

}