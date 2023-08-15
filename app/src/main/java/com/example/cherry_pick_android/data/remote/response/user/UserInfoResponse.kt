package com.example.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UserInfoResponse(
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
    @Json(name = "memberImgUrl")
    val memberImgUrl: String?,
    @Json(name = "memberNumber")
    val memberNumber: String?,
    @Json(name = "name")
    val name: String?
)