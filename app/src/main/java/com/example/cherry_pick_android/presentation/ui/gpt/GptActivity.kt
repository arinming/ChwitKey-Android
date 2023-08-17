package com.example.cherry_pick_android.presentation.ui.gpt

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.service.gpt.GptSelectService
import com.example.cherry_pick_android.data.remote.service.gpt.NewGptService
import com.example.cherry_pick_android.databinding.ActivityGptBinding
import com.example.cherry_pick_android.presentation.adapter.GptAdapter
import com.example.cherry_pick_android.presentation.adapter.Message
import com.example.cherry_pick_android.presentation.viewmodel.gpt.GptViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GptActivity: AppCompatActivity(), GptClickListener {
    companion object{
        const val TAG = "GPT Activity"
        const val SUMMARY = "기사 요약하기"
        const val TRANSLATION = "기사 영어로 번역하기"
        const val KEYWORD = "기사 키워드 보기"
    }
    @Inject
    lateinit var gptService: NewGptService
    @Inject
    lateinit var gptSelectService: GptSelectService
    private lateinit var gptAdapter: GptAdapter
    private var id = 0
    private val viewModel: GptViewModel by viewModels()

    private val binding: ActivityGptBinding by lazy {
        ActivityGptBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        id = intent.getIntExtra("id", -1)

        observeEditText()
        initRecyclerView()
        gptCreate()
        goToBack()

        // gpt 선택 창 클릭 이벤트
        viewModel.type.observe(this@GptActivity, Observer {
            Log.d(TAG, "옵저버 감지:$it")
            val service = gptSelectService

            lifecycleScope.launch {
                when(it){
                    "summary" -> {
                        gptAdapter.addMessage(SUMMARY, 2)
                        val response = service.getSelectMessage(id, it).body()
                        val statusCode = response?.statusCode
                        val answer = response?.data?.answer

                        if(statusCode == 200){
                            gptAdapter.addMessage(answer!!, 1)
                        }else{ Log.d(TAG, "통신오류: $statusCode")}
                    }
                    "translation" -> {
                        gptAdapter.addMessage(TRANSLATION, 2)
                        val response = service.getSelectMessage(id, it).body()
                        val statusCode = response?.statusCode
                        val answer = response?.data?.answer

                        if(statusCode == 200){
                            gptAdapter.addMessage(answer!!, 1)
                        }else{ Log.d(TAG, "통신오류: $statusCode")}
                    }
                    "keyword" -> {
                        gptAdapter.addMessage(KEYWORD, 2)
                        val response = service.getSelectMessage(id, it).body()
                        val statusCode = response?.statusCode
                        val answer = response?.data?.answer

                        if(statusCode == 200){
                            gptAdapter.addMessage(answer!!, 1)
                        }else{ Log.d(TAG, "통신오류: $statusCode")}
                    }
                }
            }

        })

    }

    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun gptCreate(){
        lifecycleScope.launch {
            val response = gptService.getNewGpt(id).body()
            val statusCode = response?.statusCode

            if(statusCode == 200){
                gptAdapter.addMessage(response.data?.greeting!!, 1)
                gptAdapter.addMessage("", 0)
            }
        }
    }

    private fun initRecyclerView(){
        gptAdapter = GptAdapter(this@GptActivity)
        binding.rvGptMessages.adapter = gptAdapter
        binding.rvGptMessages.layoutManager = LinearLayoutManager(this)
    }

    private fun observeEditText(){
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

    override fun onClickItem(type: String) {
        viewModel.setType(type)
    }
}