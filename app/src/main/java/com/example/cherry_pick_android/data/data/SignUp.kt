package com.example.cherry_pick_android.data.data


import com.squareup.moshi.Json

data class SignUp(
    @field:Json(name = "loginMember")
    val loginMember: LoginMember?,
    @field:Json(name = "request")
    val request: Request?
)

data class LoginMember(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?
)

data class Request(
    @field:Json(name = "birthdate")
    val birthdate: String?,
    @field:Json(name = "name")
    val name: String?
)