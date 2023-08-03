package com.example.cherry_pick_android.presentation.ui.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityArticleBinding
import com.example.cherry_pick_android.presentation.ui.gpt.GptActivity
import com.google.android.material.snackbar.Snackbar

class ArticleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private var isScrappedInit = false

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        goToBack()
        goToGPT()
        articleScrap()
    }

    fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    // GPT 버튼 이벤
    fun goToGPT() {
        binding.ibtnGpt.setOnClickListener {
            startActivity(Intent(this, GptActivity::class.java))
        }
    }

    // 스크랩 버튼 이벤트
    fun articleScrap() {
        binding.ibtnScrap.setOnClickListener {
            var isScrapped = !isScrappedInit
            // 스크랩 여부 판별해서 버튼 변경
            val changeButton = if (isScrapped) {
                isScrappedInit = true
                R.drawable.ic_scrap_true
            } else {
                isScrappedInit = false
                R.drawable.ic_scrap_false
            }

            // 스크랩 여부 판별해서 SnackBar 띄우기
            val snackbarText = if (isScrapped) {
                R.string.scrap_snackbar_true
            } else {
                R.string.scrap_snackbar_false
            }

            binding.ibtnScrap.setImageResource(changeButton)
            Snackbar.make(binding.root, snackbarText, Snackbar.LENGTH_SHORT).show()
        }
    }
}