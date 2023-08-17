package com.example.cherry_pick_android.data.remote.service.article

import com.example.cherry_pick_android.data.remote.response.article.ArticleUnlikeResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

// 관심 기사 취소 요청
interface ArticleUnlikeService {
    @DELETE("/api/articles/unlike/{articleId}")
    suspend fun deleteArticleUnlike(
        @Path("articleId") articleId: Int,
        @Query("memberId") memberId: Int,
        @Query("type") type: String
    ): Response<ArticleUnlikeResponse>
}