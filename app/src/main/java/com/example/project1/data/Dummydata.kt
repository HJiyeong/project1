/*

package com.example.project1.data

import com.example.project1.R
import com.example.project1.model.*


object Dummydata {
    fun getCafeDrawable(index: Int): Int =
        R.drawable::class.java.getField("img_cafe_sample$index").getInt(null)
    fun getCafeInfoByID(cid: String): CafeInfo =
        DummyCafeInfo.first { it.cid == cid }

    val DummyCafeName = listOf("스타벅스 대전유성구청점","홀리크로스","카페스테이인터뷰 대전신세계 Art&Science점","메가커피 대전충남대정문점","스타벅스 대전유성구청점","스타벅스 대전유성구청점","스타벅스 대전유성구청점","카페 에이트")
    val DummyCafeAddress = listOf("대전 유성구 어은동","대전 서구 둔산동","대전 유성구 도룡동","대전 유성구 궁동","대전 유성구 어은동", "대전 유성구 어은동","대전 유성구 어은동","대전 유성구 궁동")

    val DummyCafeInfo = (1..8).map { i ->
        CafeInfo(
            cid = i.toString(),
            name = DummyCafeName[i-1],
            imageURL = getCafeDrawable(i),
            shortAddress = DummyCafeAddress[i-1]
        )
    }

    val DummyCafeListName = listOf("카공", "데이트", "인스타", "저가", "살려주세요")
    val DummyCafeListContents = listOf(
        listOf(1,4,5,7,8),
        listOf(2,3,8),
        listOf(2,3,4,6,7,8),
        listOf(1,4,5),
        listOf(1,2,3,4,5,6,7,8)
    )
    val DummyCafeList = (1 .. 5).map { i ->
        val cafeIndexes = DummyCafeListContents[i-1]
        val cafes = cafeIndexes.map { DummyCafeInfo[it-1] }
        CafeList(
            cid = i.toString(),
            name = DummyCafeListName[i-1],
            imageURL = cafes[0].imageURL,
            list = cafes
        )
    }

    val DummyUserName = listOf("황지영", "신우혁", "A", "B")
    val DummyFollowerContents = listOf(
        listOf(2,3,4), emptyList(), emptyList(), emptyList()
    )
    val CafeListContents = listOf(
        listOf(1,2,3), listOf(2,4,5), emptyList(), emptyList()
    )
    val RecommendContents = listOf(
        listOf(1,4,5,6), listOf(3,4,5,6), listOf(1,2), listOf(1,5,7,8)
    )

    val DummyFollowers = (1..3).map{i ->
        User(
            uid = (i+1).toString(),
            name = DummyUserName[i],
            followers = emptyList(),
            cafeLists = CafeListContents[i].map { DummyCafeList[it - 1] },
            recommendation = RecommendContents[i].map { DummyCafeInfo[it - 1] }
        )
    }
    val User0 =
        User(
            uid = "1",
            name = DummyUserName[0],
            followers = DummyFollowers,
            cafeLists = CafeListContents[0].map { DummyCafeList[it - 1] },
            recommendation = RecommendContents[1].map { DummyCafeInfo[it - 1] }
        )
}


*/