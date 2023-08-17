package com.example.cherry_pick_android.data.remote.request.user


import com.squareup.moshi.Json

data class updateIndustryRequest(
    @Json(name = "updateIndustryReq")
    val updateIndustryReq: UpdateIndustryReq?
)

data class UpdateIndustryReq(
    @Json(name = "industryKeyword1")
    val industryKeyword1: String?,
    @Json(name = "industryKeyword2")
    val industryKeyword2: String?,
    @Json(name = "industryKeyword3")
    val industryKeyword3: String?
)