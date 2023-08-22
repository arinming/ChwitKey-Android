package com.chwitkey.cherry_pick_android.data.remote.response

import com.google.gson.annotations.SerializedName

abstract class BaseResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean = false,
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String? = null
)