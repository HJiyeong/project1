package com.example.project1.model
import com.google.gson.annotations.SerializedName

data class FeedResponse (
    @SerializedName("feed_id") val fid: Int,
    val user: User,
    val cafe: CafeInfo,
    val likes: Int,

    val comments: List<String>
)