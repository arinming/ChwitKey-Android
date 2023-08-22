package com.chwitkey.cherry_pick_android.data.remote.service.article

import com.chwitkey.cherry_pick_android.data.remote.response.article.ArticleDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// 기사 상세조회 요청
interface ArticleDetailService {
    @GET("/api/articles/detail/{articleId}")
    suspend fun getArticleDetail(
        @Path("articleId") articleId: Int
    ) : Response<ArticleDetailResponse>
}