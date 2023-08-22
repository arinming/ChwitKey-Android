package com.chwitkey.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UserKeywordResponse(
    @field:Json(name = "data")
    val `data`: Data?,
    @field:Json(name = "description")
    val description: Any?,
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?
)

data class Data(
    @field:Json(name = "keywordList")
    val keywordList: List<Any?>?
)