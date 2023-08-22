package com.example.cherry_pick_android.presentation.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.service.article.ArticleDetailService
import com.example.cherry_pick_android.data.remote.service.article.ArticleLikeService
import com.example.cherry_pick_android.data.remote.service.article.ArticleUnlikeService
import com.example.cherry_pick_android.databinding.ActivityArticleBinding
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
    private var isScraped = false
    private var isLiked = false
    private var id = 0

    @Inject
    lateinit var articleDetailService: ArticleDetailService
    @Inject
    lateinit var articleLikeService: ArticleLikeService
    @Inject
    lateinit var articleUnlikeService: ArticleUnlikeService

    companion object {
        const val TAG = "ArticleActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initArticle()
        getDetailArticle()
        goToBack()
        goToGPT()
        goToLink()
        articleScrap()
        articleLike()
        articleShare()
    }

    // 인텐트 값으로 articleID 받기
    private fun initArticle() {
        val articleIntent: Intent = intent
        id = articleIntent.getIntExtra("id", 0)
    }

    private fun getDetailArticle() {
        // API 통신
        lifecycleScope.launch {
            val response = articleDetailService.getArticleDetail(id)
            val statusCode = response.body()?.statusCode
            withContext(Dispatchers.Main) {
                if (statusCode == 200) {
                    response.body()?.data?.articlePhoto?.map { image ->
                        val imageUrl = image.articleImgUrl.ifEmpty { "" } // 기사 사진이 없으면 빈 문자열로 처리
                        val imageDesc = image.imgDesc.ifEmpty { "이미지 캡션이 없습니다." }

                        Glide.with(this@ArticleActivity)
                            .load(imageUrl)
                            .into(binding.ivNewsImage)
                        binding.ivNewsImage.visibility = View.VISIBLE
                        binding.tvExplainImage.text = imageDesc
                    }
                    if (response.body()?.data?.articlePhoto?.size == 0) {
                        // 사진이 없으면 사진 지우기
                        binding.ivNewsImage.visibility = View.GONE
                        binding.tvExplainImage.visibility = View.GONE
                    }
                    binding.tvArticleTitle.text = response.body()?.data?.title
                    binding.tvArticleCompany.text = response.body()?.data?.publisher
                    binding.tvArticleEditor.text = response.body()?.data?.reporter

                    // 엔터 적용 및 줄바꿈 처리
                    val content = response.body()?.data?.content ?: ""
                    val sentences = content.split("\\.\\s\\n*".toRegex()) // 문장 분리
                    val firstTwoSentences = sentences.take(3).joinToString(". ") // 3문장 까지만

                    binding.tvArticleDetail.text = firstTwoSentences.replace("\\n", "\n")

                    binding.tvArticleTime.text = response.body()?.data?.uploadedAt

                    // 스크랩 or 좋아요 여부
                    val isScrap = response.body()?.data?.isScrapped!!
                    val isLike = response.body()?.data?.isLiked!!
                    isScraped = isScrap
                    isLiked = isLike

                    // 스크랩, 좋아요 표시
                    loadIsScraped(isScrap)
                    loadIsLiked(isLike)

                } else {
                    Snackbar.make(binding.root, "오류가 발생했습니다. (Code: $statusCode)", Snackbar.LENGTH_SHORT).show()
                }
                binding.lottieDotLoading.visibility = View.GONE
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
            Log.d(TAG, "SCRAP:$isScraped")
            // 스크랩 여부 판별해서 버튼 변경
            var changeScrapButton = 0
            var snackbarText = 0
            var likeStatusCode = 0
            var unStatusCode = 0

            lifecycleScope.launch(Dispatchers.Main) {
                if(!isScraped){
                    likeStatusCode = articleLikeService.postArticleLike(id, "scrap").body()?.statusCode ?: 0
                    if(likeStatusCode == 200){
                        changeScrapButton = R.drawable.ic_scrap_true
                        snackbarText = R.string.scrap_snackbar_true
                        isScraped = true
                    }else{
                        snackbarText = R.string.scrap_and_like_error
                    }
                }else{
                    unStatusCode = articleUnlikeService.deleteArticleUnlike(id, "scrap").body()?.statusCode ?: 0
                    if(unStatusCode == 200){
                        changeScrapButton = R.drawable.ic_scrap_false
                        snackbarText = R.string.scrap_snackbar_false
                        isScraped = false
                    }else{
                        snackbarText = R.string.scrap_and_like_error
                    }
                }
                binding.ibtnScrap.setImageResource(changeScrapButton)
                Snackbar.make(binding.root, snackbarText, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // 좋아요 버튼 이벤트
    private fun articleLike() {
        binding.ibtnLike.setOnClickListener {
            var changeLikeButton = 0

            lifecycleScope.launch {
                if(!isLiked){
                    val statusCode = articleLikeService.postArticleLike(id, "like").body()?.statusCode ?: 0
                    if(statusCode == 200){
                        changeLikeButton = R.drawable.ic_like_true
                        isLiked = true
                    }else{Snackbar.make(binding.root, "오류가 발생했습니다. (Code: $statusCode)", Snackbar.LENGTH_SHORT).show()}
                }else{
                    val statusCode = articleUnlikeService.deleteArticleUnlike(id, "like").body()?.statusCode ?: 0
                    if(statusCode == 200){
                        changeLikeButton = R.drawable.ic_like_false
                        isLiked = false
                    }else{Snackbar.make(binding.root, "오류가 발생했습니다. (Code: $statusCode)", Snackbar.LENGTH_SHORT).show()}
                }
                withContext(Dispatchers.Main){
                    binding.ibtnLike.setImageResource(changeLikeButton)
                }
            }
        }
    }

    // 공유하기 버튼 이벤트
    private fun articleShare() {
        lifecycleScope.launch(Dispatchers.Main){
            val response = articleDetailService.getArticleDetail(id).body()

            binding.ibtnShare.setOnClickListener {
                val articleShareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    // 전달하려는 데이터 값
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "${response?.data?.url}"
                    )
                    type = "text/plain"
                }

                startActivity(Intent.createChooser(articleShareIntent, null))
            }

        }
    }

    // 해당 기사 링크 이동
    private fun goToLink() {
        binding.btnLink.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    val response = articleDetailService.getArticleDetail(id)
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(response.body()?.data?.url)
                    )
                    startActivity(urlIntent)
                }
            }
        }
    }

    private fun loadIsScraped(it: Boolean){
        if(it){
            binding.ibtnScrap.setImageResource(R.drawable.ic_scrap_true)
        }else{
            binding.ibtnScrap.setImageResource(R.drawable.ic_scrap_false)
        }
    }

    private fun loadIsLiked(it: Boolean){
        if(it){
            binding.ibtnLike.setImageResource(R.drawable.ic_like_true)
        }else{
            binding.ibtnLike.setImageResource(R.drawable.ic_like_false)
        }
    }

}