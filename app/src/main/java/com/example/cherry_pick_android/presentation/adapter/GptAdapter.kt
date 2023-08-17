package com.example.cherry_pick_android.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemGptBubbleBinding
import com.example.cherry_pick_android.databinding.ItemGptUserBubbleBinding


class GptAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        const val TAG = "GptAdapter"
    }
    private val messageList = mutableListOf<Message>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "OnCreateViewHolder/ viewType:$viewType")
        // 1: 사용자, else: GPT로 뷰홀더 반환
        return when(viewType){
            1 -> {
                val binding = ItemGptUserBubbleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserMessageViewHolder(binding)
            }
            else -> {
                val binding = ItemGptBubbleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GptMessageViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = messageList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        Log.d(TAG, "[OnBindViewHolder] message:$message")
        if(message.isUser){
            (holder as UserMessageViewHolder).bindUserMessage(message.content)
        }else{
            (holder as GptMessageViewHolder).bindGPTMessage(message.content)
        }

    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "gptItemViewType")
        return if (messageList[position].isUser) 1 else 0
    }

    inner class UserMessageViewHolder(val binding: ItemGptUserBubbleBinding): RecyclerView.ViewHolder(binding.root){
        // 사용자 말풍선 우측 배치
        fun bindUserMessage(content: String){
            binding.tvContentUser.text = content
            binding.root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                startToStart = ConstraintLayout.LayoutParams.UNSET
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                marginEnd = 20
            }
        }

    }

    inner class GptMessageViewHolder(val binding: ItemGptBubbleBinding): RecyclerView.ViewHolder(binding.root){
        val content = binding.tvContentGpt
        fun bindGPTMessage(content: String){
            binding.tvContentGpt.text = content
            binding.root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                marginStart = 20
                marginEnd = 50
                topMargin = 24
            }
        }
    }

    fun addMessage(content: String, isUser: Boolean){
        Log.d(TAG, "addMessage 감지")
        val newMessage = Message(content, isUser)
        messageList.add(newMessage)
        notifyItemInserted(messageList.size - 1)
        //notifyDataSetChanged()
    }

}

data class Message(
    val content: String,
    val isUser: Boolean
)

