package com.example.cherry_pick_android.presentation.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.service.article.ArticleDetailService
import com.example.cherry_pick_android.databinding.ActivityArticleBinding
import com.example.cherry_pick_android.presentation.adapter.ArticleItem
import com.example.cherry_pick_android.presentation.adapter.NewsRecyclerViewAdapter
import com.example.cherry_pick_android.presentation.ui.gpt.GptActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding

    // 좋아요, 스크랩 초기화
    private var isScrappedInit = false
    private var isLikeInit = false
    private var id = 0

    @Inject
    lateinit var articleDetailService: ArticleDetailService

    companion object{
        const val TAG = "ArticleActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getDetailArticle()
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
        id = articleIntent.getIntExtra("id", 0)

//        binding.tvArticleTitle.text = articleTitle.toString()
//        binding.tvArticleCompany.text = articleCompany.toString()
//        binding.tvArticleTime.text = articleTime.toString()

    }

    private fun getDetailArticle() {
        // API 통신
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val response = articleDetailService.getArticleDetail(9312)
                val statusCode = response.body()?.statusCode
                if (statusCode == 200) {
                    binding.tvArticleTitle.text = response.body()?.data?.title
                    binding.tvArticleCompany.text = response.body()?.data?.publisher
                    binding.tvArticleEditor.text = response.body()?.data?.reporter
                    // 엔터 적용
                    binding.tvArticleDetail.text = response.body()?.data?.content?.replace("\\n", "\n")
                    binding.tvArticleTime.text = response.body()?.data?.uploadedAt
                } else {
                    Toast.makeText(this@ArticleActivity, "에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }



    // GPT 버튼 이벤트
    private fun goToGPT() {
        val intent = Intent(this, GptActivity::class.java)

        intent.putExtra("id", id)
        binding.ibtnGpt.setOnClickListener {
            startActivity(intent)
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