package com.example.project1.model

import com.google.gson.annotations.SerializedName

data class CafeInfo(
    @SerializedName("cafe_id") val cid: Int = 0,
    val name: String = "",
    @SerializedName("image_url") val imageURL: String = "",
    @SerializedName("short_address") val shortAddress: String = "",
    @SerializedName("cafe_url") val cafeURL: String = "",
    @SerializedName("cafe_introduce") val cafeIntroduce: String = "",
    @SerializedName("amenities") val amenities: String = ""
)