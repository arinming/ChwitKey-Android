package com.chwitkey.cherry_pick_android.data.remote.request.user


import com.squareup.moshi.Json

data class InitUserSaveRequest(
    @field:Json(name = "birth")
    val birth: String?,
    @field:Json(name = "gender")
    val gender: String?,
    @field:Json(name = "industryKeywords")
    val industryKeywords: List<IndustryKeyword?>?,
    @field:Json(name = "nickname")
    val nickname: String?
)
data class IndustryKeyword(
    @field:Json(name = "industryKeyword")
    val industryKeyword: String?
)