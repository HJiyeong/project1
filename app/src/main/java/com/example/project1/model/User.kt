package com.example.project1.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val uid: Int,
    val name: String = "",
    @SerializedName("follows") val followers: List<User> = emptyList(),
    @SerializedName("cafe_lists") val cafeLists: List<CafeList> = emptyList(),
    @SerializedName("recommends") val recommendation: List<CafeInfo> = emptyList()
)
