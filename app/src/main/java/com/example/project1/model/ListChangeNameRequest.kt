package com.example.project1.model
import com.google.gson.annotations.SerializedName

data class ListChangeNameRequest (
    @SerializedName("list_id") val cid: Int,
    val name: String
)