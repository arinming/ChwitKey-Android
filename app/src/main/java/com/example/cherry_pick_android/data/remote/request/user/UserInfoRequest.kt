package com.example.cherry_pick_android.data.remote.request.user


import com.squareup.moshi.Json

data class UserInfoRequest(
    @Json(name = "birth")
    val birth: String?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "industryKeywords")
    val industryKeywords: List<IndustryKeyword?>?,
    @Json(name = "nickname")
    val nickname: String?
)
data class IndustryKeyword(
    @Json(name = "industryKeyword")
    val industryKeyword: String?
)