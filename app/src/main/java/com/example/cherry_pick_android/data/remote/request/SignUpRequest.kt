package com.example.cherry_pick_android.data.remote.request


import com.squareup.moshi.Json

data class SignUpRequest(
    @field:Json(name = "request")
    val request: Request?
)
data class Request(
    @field:Json(name = "birthdate")
    val birthdate: String?,
    @field:Json(name = "name")
    val name: String?
)