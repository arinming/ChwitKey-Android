package com.example.cherry_pick_android.data.remote.response


import com.squareup.moshi.Json

data class SignUpResponse(
    @field:Json(name = "birthdate")
    val birthdate: String?,
    @field:Json(name = "gender")
    val gender: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "industryKeyword")
    val industryKeyword: IndustryKeyword?,
    @field:Json(name = "keywords")
    val keywords: List<String?>?,
    @field:Json(name = "name")
    val name: String?
)

data class IndustryKeyword(
    @field:Json(name = "first")
    val first: String?,
    @field:Json(name = "second")
    val second: String?,
    @field:Json(name = "third")
    val third: String?
)