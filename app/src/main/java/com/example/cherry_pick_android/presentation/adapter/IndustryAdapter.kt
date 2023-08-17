package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.databinding.ItemKeywordBtnBinding
import com.example.cherry_pick_android.presentation.ui.article.ArticleActivity
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.home.homeNews.HomeNewsFragment
import kotlinx.coroutines.CoroutineScope
class IndustryAdapter(private val industries: List<String>) :
    RecyclerView.Adapter<IndustryAdapter.ViewHolder>() {

    private var selectedPosition = 0 // 선택된 버튼의 위치 추적 변수

    inner class ViewHolder(private val binding: ItemKeywordBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var industry = binding.btnKeyword
        var isSelected = false // 추가: 버튼이 선택되었는지 여부를 추적하는 변수

        init {
            binding.btnKeyword.setOnClickListener { view ->
                val industryText = industry.text.toString() // 클릭한 버튼의 텍스트 가져오기
                Log.d("직군 버튼 클릭", industryText)

                // 선택된 버튼의 위치 업데이트
                val clickedPosition = adapterPosition
                if (selectedPosition != clickedPosition) {
                    isSelected = true
                    notifyItemChanged(selectedPosition)
                    selectedPosition = clickedPosition
                } else {
                    isSelected = !isSelected
                }
                updateButtonStyles()

                // 클릭한 버튼의 텍스트를 HomeNewsFragment로 전달하여 사용
                (view.context as? HomeNewsFragment)?.onIndustryButtonClick(industryText)
            }
        }

        // 버튼 스타일 업데이트 함수
        fun updateButtonStyles() {
            if (isSelected) {
                industry.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                industry.setBackgroundResource(R.drawable.bg_keyword)
            } else {
                industry.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_7))
                industry.setBackgroundResource(R.drawable.bg_keyword_false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeywordBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val industryItem = industries[position]
        holder.industry.text = industryItem
        holder.isSelected = selectedPosition == position
        holder.updateButtonStyles()
    }

    override fun getItemCount(): Int = industries.size
}
