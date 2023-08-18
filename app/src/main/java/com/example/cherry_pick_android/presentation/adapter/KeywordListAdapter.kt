package com.example.cherry_pick_android.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.model.KeywordEntity
import com.example.cherry_pick_android.databinding.ItemKeywordCompleteBtnBinding
import com.example.cherry_pick_android.presentation.ui.keyword.DeleteListener

class KeywordListAdapter (private val deleteListener: DeleteListener): RecyclerView.Adapter<KeywordListAdapter.KeywordListHolder>() {
    private val items = ArrayList<KeywordEntity>()
    private var selectedPosition = 0 // 선택된 버튼의 위치 추적 변수, 초기값 -1로 설정

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordListHolder {
        val binding = ItemKeywordCompleteBtnBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return KeywordListHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: KeywordListHolder, position: Int) {
        val keywordEntity = items[position]
        holder.setItem(keywordEntity.keyword)

        // 버튼 색상 설정
        val isSelected = keywordEntity.isSelected
        val button = holder.binding.rlKeywordComplete
        if (isSelected) {
            button.setBackgroundResource(R.drawable.bg_keyword)
        } else {
            button.setBackgroundResource(R.drawable.bg_keyword_false)
        }

        // 클릭 이벤트 핸들러에서 선택된 버튼의 위치를 업데이트하고 어댑터를 갱신
        holder.itemView.setOnClickListener { view ->
            val clickedPosition = holder.adapterPosition
            if (selectedPosition != clickedPosition) {
                items[selectedPosition].isSelected = false
                selectedPosition = clickedPosition
                keywordEntity.isSelected = true
                notifyDataSetChanged()
            }
        }

        holder.binding.btnClearKeyword.setOnClickListener {
            Log.d("SearchKeywordAdapter", "clearBtnClick")
            deleteListener.onDeleteClick(keywordEntity.keyword)
        }
    }

    inner class KeywordListHolder(val binding: ItemKeywordCompleteBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var keyword = binding.btnKeyword
        var isSelected = false // 추가: 버튼이 선택되었는지 여부를 추적하는 변수

        init {
            keyword.setOnClickListener { view ->
                val keywordText = keyword.text.toString()
                Log.d("키워드 버튼 클릭", keywordText)

                // 선택된 버튼의 위치 업데이트
                val clickedPosition = adapterPosition
                if (selectedPosition != clickedPosition) {
                    isSelected = true
                    notifyItemChanged(selectedPosition)
                    selectedPosition = clickedPosition
                }

            }
        }

        fun setItem(keyword: String) {
            binding.btnKeyword.text = keyword
        }
    }

    // 리스트 설정
    fun setList(list: List<KeywordEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}