package com.chwitkey.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UserInfoResponse(
    @field:Json(name = "data")
    val `data`: UserData?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "statusCode")
    val statusCode: Int?,
    @field:Json(name = "transaction_time")
    val transactionTime: String?

)

data class UserData(
    @field:Json(name = "birthdate")
    val birthdate: String?,
    @field:Json(name = "gender")
    val gender: String?,
    @field:Json(name = "industryKeyword1")
    val industryKeyword1: String?,
    @field:Json(name = "industryKeyword2")
    val industryKeyword2: String?,
    @field:Json(name = "industryKeyword3")
    val industryKeyword3: String?,
    @field:Json(name = "memberImgUrl")
    val memberImgUrl: String?,
    @field:Json(name = "memberNumber")
    val memberNumber: String?,
    @field:Json(name = "name")
    val name: String?
)