package com.example.project1.utils

import com.example.project1.R

fun getRandomCafeDrawable(): Int {
    val images = listOf(
        R.drawable.img_cafe_sample1,
        R.drawable.img_cafe_sample2,
        R.drawable.img_cafe_sample3,
        R.drawable.img_cafe_sample4,
        R.drawable.img_cafe_sample5,
        R.drawable.img_cafe_sample6,
        R.drawable.img_cafe_sample7,
        R.drawable.img_cafe_sample8,
        R.drawable.img_cafe_sample9,
        R.drawable.img_cafe_sample10,
        R.drawable.img_cafe_sample11,
        R.drawable.img_cafe_sample12,
        R.drawable.img_cafe_sample13,
        R.drawable.img_cafe_sample14
    )
    return images.random()
}