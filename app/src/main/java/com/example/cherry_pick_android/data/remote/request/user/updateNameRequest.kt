package com.example.cherry_pick_android.data.remote.request.user


import com.squareup.moshi.Json

data class updateNameRequest(
    @field:Json(name = "updateNameReq")
    val updateNameReq: UpdateNameReq?
)
data class UpdateNameReq(
    @field:Json(name = "updateName")
    val updateName: String?
)