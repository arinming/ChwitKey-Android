package com.example.cherry_pick_android.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.databinding.ItemGptBubbleBinding
import com.example.cherry_pick_android.databinding.ItemGptChoiceBinding
import com.example.cherry_pick_android.databinding.ItemGptUserBubbleBinding
import com.example.cherry_pick_android.presentation.ui.gpt.GptClickListener


class GptAdapter(private val listener: GptClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        const val TAG = "GptAdapter"
    }
    private val messageList = mutableListOf<Message>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "OnCreateViewHolder/ viewType:$viewType")
        // 0: 최초 선택 1: gpt 2: 유저에 따른 반환
        return when(viewType){
            0 ->{
                val binding = ItemGptChoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GptChoiceViewHolder(binding)
            }
            1 -> {
                val binding = ItemGptBubbleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GptMessageViewHolder(binding)
            }
            else -> {
                val binding = ItemGptUserBubbleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserMessageViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = messageList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        Log.d(TAG, "[OnBindViewHolder] message:$message")

        when(message.isUser){
            0 -> {(holder as GptChoiceViewHolder).bindChoice()}
            1 -> {(holder as GptMessageViewHolder).bindGPTMessage(message.content)}
            else -> {(holder as UserMessageViewHolder).bindUserMessage(message.content)}
        }

    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "gptItemViewType")
        return messageList[position].isUser
    }

    // 유저 뷰홀더
    inner class UserMessageViewHolder(val binding: ItemGptUserBubbleBinding): RecyclerView.ViewHolder(binding.root){
        // 사용자 말풍선 우측 배치
        fun bindUserMessage(content: String){
            binding.tvContentUser.text = content
        }

    }

    // gpt 뷰 홀더
    inner class GptMessageViewHolder(val binding: ItemGptBubbleBinding): RecyclerView.ViewHolder(binding.root){
        val content = binding.tvContentGpt
        fun bindGPTMessage(content: String){
            binding.tvContentGpt.text = content
        }
    }

    // 최초 선택 뷰 홀더
    inner class GptChoiceViewHolder(val binding: ItemGptChoiceBinding): RecyclerView.ViewHolder(binding.root){
        init {
            with(binding){
                tvSummary.setOnClickListener {
                    listener.onClickItem("summary")
                }
                tvTranslation.setOnClickListener {
                    listener.onClickItem("translation")
                }
                tvKeyword.setOnClickListener {
                    listener.onClickItem("keyword")
                }
            }
        }
        fun bindChoice(){
            binding.root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 20
                marginEnd = 20
                topMargin = 20
            }
        }


    }


    fun addMessage(content: String, isUser: Int){
        Log.d(TAG, "addMessage 감지")
        val newMessage = Message(content, isUser)
        messageList.add(newMessage)
        notifyItemInserted(messageList.size - 1)
    }

}

// isUser 규칙 - 0: 최초 선택 기능 1: gpt 2: 유저
data class Message(
    val content: String,
    val isUser: Int
)

