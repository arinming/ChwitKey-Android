package com.example.cherry_pick_android.data.remote.repository

import androidx.lifecycle.MutableLiveData
import com.example.cherry_pick_android.data.data.Pageable
import com.example.cherry_pick_android.data.remote.response.ArticleCommendResponse
import com.example.cherry_pick_android.data.remote.service.ArticleSearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleSearchService: ArticleSearchService
) {
    suspend fun makeApiCall(liveDataList: MutableLiveData<List<ArticleCommendResponse.Data>>) {
        val call: Call<ArticleCommendResponse> =
            articleSearchService.getArticleCommend("", "", Pageable)
        call?.enqueue(object : Callback<ArticleCommendResponse> {
            override fun onResponse(
                call: Call<ArticleCommendResponse>,
                response: Response<ArticleCommendResponse>
            ) {
                liveDataList.postValue(response.body()?.data)
            }

            override fun onFailure(call: Call<ArticleCommendResponse>, t: Throwable) {
                liveDataList.postValue(null)
            }
        })

    }
}

private fun <T> MutableLiveData<T>.postValue(data: ArticleCommendResponse.Data?) {

}
