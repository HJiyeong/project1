package com.example.project1.model
import com.google.gson.annotations.SerializedName

data class CafeList (
    @SerializedName("list_id") val cid: Int,
    val name: String = "",
    @SerializedName("image_url") val imageURL: String = "",
    @SerializedName("contains") val list: List<CafeInfo> = emptyList()
)