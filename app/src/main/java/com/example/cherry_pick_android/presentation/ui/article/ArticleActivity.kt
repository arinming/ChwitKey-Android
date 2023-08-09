package com.example.cherry_pick_android.presentation.ui.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityArticleBinding
import com.example.cherry_pick_android.presentation.ui.gpt.GptActivity
import com.google.android.material.snackbar.Snackbar

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding

    // 좋아요, 스크랩 초기화
    private var isScrappedInit = false
    private var isLikeInit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initArticle()
        goToBack()
        goToGPT()
        articleScrap()
        articleLike()
        articleShare()
    }

    // 인텐트 값 받아서 초기화
    private fun initArticle() {
        val articleIntent: Intent = intent
        val articleTitle: String? = articleIntent.getStringExtra("제목")
        val articleCompany: String? = articleIntent.getStringExtra("회사")
        val articleTime: String? = articleIntent.getStringExtra("시간")

        binding.tvArticleTitle.text = articleTitle.toString()
        binding.tvArticleCompany.text = articleCompany.toString()
        binding.tvArticleTime.text = articleTime.toString()

    }


    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }



    // GPT 버튼 이벤트
    private fun goToGPT() {
        binding.ibtnGpt.setOnClickListener {
            startActivity(Intent(this, GptActivity::class.java))
        }
    }

    // 스크랩 버튼 이벤트
    private fun articleScrap() {
        binding.ibtnScrap.setOnClickListener {
            val isScrapped = !isScrappedInit
            // 스크랩 여부 판별해서 버튼 변경
            val changeScrapButton = if (isScrapped) {
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

            binding.ibtnScrap.setImageResource(changeScrapButton)
            Snackbar.make(binding.root, snackbarText, Snackbar.LENGTH_SHORT).show()
        }
    }

    // 좋아요 버튼 이벤트
    private fun articleLike() {
        binding.ibtnLike.setOnClickListener {
            val isLike = !isLikeInit

            // 좋아요 여부 판별해서 버튼 변경
            val changeLikeButton = if (isLike) {
                isLikeInit = true
                R.drawable.ic_like_true
            } else {
                isLikeInit = false
                R.drawable.ic_like_false
            }

            binding.ibtnLike.setImageResource(changeLikeButton)
        }
    }

    // 공유하기 버튼 이벤트
    private fun articleShare() {
        binding.ibtnShare.setOnClickListener {
            val articleShareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                // 전달하려는 데이터 값
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${R.string.article_news_title}"
                )
                type = "text/plain"
            }

            startActivity(Intent.createChooser(articleShareIntent, null))
        }
    }
}