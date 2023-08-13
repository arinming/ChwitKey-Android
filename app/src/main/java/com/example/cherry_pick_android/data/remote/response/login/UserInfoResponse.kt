package com.example.cherry_pick_android.data.remote.response.login


import com.squareup.moshi.Json

data class UserInfoResponse(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "description")
    val description: Any?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "statusCode")
    val statusCode: Int?,
    @Json(name = "transaction_time")
    val transactionTime: String?
)

data class Data(
    @Json(name = "birthdate")
    val birthdate: String?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "industryKeyword1")
    val industryKeyword1: String?,
    @Json(name = "industryKeyword2")
    val industryKeyword2: String?,
    @Json(name = "industryKeyword3")
    val industryKeyword3: String?,
    @Json(name = "memberNumber")
    val memberNumber: String?,
    @Json(name = "name")
    val name: String?
)