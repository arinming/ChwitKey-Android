package com.example.cherry_pick_android.presentation.ui.gpt

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityGptBinding

class GptActivity: AppCompatActivity() {
    companion object{
        const val TAG = "GPT Activity"
    }
    private val binding: ActivityGptBinding by lazy {
        ActivityGptBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")
        // 텍스트 감지
        binding.etSendMessage.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 호출 전 로직
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()){
                    // EditText에 텍스트가 입력되었을 때 전송 버튼 색 변경
                    Log.d(TAG, "입력 변경감지")
                    binding.ibtnGptSend.setImageResource(R.drawable.ic_gpt_truesend)
                }else{
                    // EditText에 텍스트가 입력되지 않았다면 원래 색으로 변경
                    binding.ibtnGptSend.setImageResource(R.drawable.ic_gpt_send)
                }
            }
            override fun afterTextChanged(p0: Editable?) {
                // 호출 후 로직
            }

        })
    }
}