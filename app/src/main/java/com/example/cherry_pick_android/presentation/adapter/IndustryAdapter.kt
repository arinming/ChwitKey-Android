package com.example.cherry_pick_android.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemKeywordBtnBinding

class IndustryAdapter(private val industries: List<String>) :
    RecyclerView.Adapter<IndustryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemKeywordBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var industry = binding.btnKeyword

        init {
            binding.root.setOnClickListener { view ->
                Toast.makeText(binding.root.context, "클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeywordBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val industryItem = industries?.get(position)
        holder.industry.text = industryItem
    }

    override fun getItemCount(): Int = industries.size

}