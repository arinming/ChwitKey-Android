package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemScrapNewsBinding
import com.example.cherry_pick_android.data.data.ScrapNews
import com.example.cherry_pick_android.presentation.ui.article.ArticleActivity
import com.google.android.material.snackbar.Snackbar

class ScrapAdapter(private val scrapNewsData: MutableList<ScrapNews>) :
    RecyclerView.Adapter<ScrapAdapter.ViewHolder>() {

    // 뷰 유형에 대한 참조 클래스
    class ViewHolder(val binding: ItemScrapNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setNewsItem(scrapNewsData: String) {
            binding.tvNewsTitle.text = scrapNewsData
        }
        // 뉴스 클릭 이벤트 설정
        init {
            binding.root.setOnClickListener { view ->
                val context = view.context
                val intent = Intent(context, ArticleActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    // 아이템 레이아웃 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScrapNewsBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    // 호출한 내용으로 bind
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setNewsItem(scrapNewsData[position].scrapNews)

        // 스크랩 버튼 클릭 이벤트 설정
        viewHolder.binding.ibtnScrap.setOnClickListener {
            scrapNewsData.removeAt(position) // 아이템 삭제
            Snackbar.make(it, "스크랩 목록에서 삭제했어요!", Snackbar.LENGTH_SHORT).show()
            notifyItemRemoved(position) // 삭제된 아이템 위치 업데이트
            notifyItemRangeChanged(position, scrapNewsData.size)  // 아이템 사이즈 업데이트
        }
    }

    // 데이터 크기 반환
    override fun getItemCount() = scrapNewsData.size

}