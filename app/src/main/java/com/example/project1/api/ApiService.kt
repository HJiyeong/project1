package com.example.project1.api
import com.example.project1.model.CafeInfo
import retrofit2.Response
import com.example.project1.model.User
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @POST("users/{id}/follow")
    suspend fun followUser(@Path("id") userId: Int): Response<Unit>

    @GET("cafes/{id}")
    suspend fun getCafeById(@Path("id") cafeId: Int): CafeInfo
}
