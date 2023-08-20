package com.example.cherry_pick_android.data.data

data class Pageable(

    var page: Int,
    var size: Int = 10,
    var sort: String
)
