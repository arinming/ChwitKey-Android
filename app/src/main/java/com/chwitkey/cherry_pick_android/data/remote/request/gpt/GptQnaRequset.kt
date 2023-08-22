package com.chwitkey.cherry_pick_android.data.remote.request.gpt


import com.squareup.moshi.Json

data class GptQnaRequset(
    @field:Json(name = "chatId")
    val chatId: Int?,
    @field:Json(name = "question")
    val question: String?
)