package com.example.cherry_pick_android.data.remote.service.user

import com.example.cherry_pick_android.data.remote.request.user.UpLoadImageRequest
import com.example.cherry_pick_android.data.remote.response.user.UpLoadImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UpLoadImageService {
    @Multipart
    @POST("/api/members/uploadImage")
    fun putUserImage(
        @Part image: MultipartBody.Part?,
        @Part("image") upLoadImageRequest: UpLoadImageRequest
    ): Call<UpLoadImageResponse>
}