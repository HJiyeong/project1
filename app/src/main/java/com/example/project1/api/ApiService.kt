package com.example.project1.api
import com.example.project1.model.CafeInfo
import com.example.project1.model.CafeList
import com.example.project1.model.ListChangeNameRequest
import com.example.project1.model.ListCreateRequest
import com.example.project1.model.User
import com.example.project1.model.UserCreateRequest
import com.example.project1.model.UserCreateResponse
import com.example.project1.model.LoginRequest
import com.example.project1.model.LoginResponse
import com.example.project1.model.PromptRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/recommend")
    suspend fun recommendUsers(@Header("Authorization") authHeader: String): List<User>

    @POST("users/{id}/follow")
    suspend fun followUser(@Path("id") userId: Int, @Header("Authorization") authHeader: String)
    : Response<Unit>

    @GET("cafes")
    suspend fun getAllCafes(): List<CafeInfo>

    @GET("cafes/default")
    suspend fun getDefaultCafes(): List<CafeInfo>

    @GET("cafes/{id}")
    suspend fun getCafeById(@Path("id") cafeId: Int): CafeInfo

    @POST("cafes/recommend")
    suspend fun recommendCafes(@Body prompt: PromptRequest): List<CafeInfo>

    @GET("users/likes/{cafe_id}")
    suspend fun isFollowingCafe(@Path("cafe_id") cid: Int, @Header("Authorization") authHeader: String): Boolean

    @POST("users/{cafe_id}/followcafe")
    suspend fun followCafe(@Path("cafe_id") cid: Int, @Header("Authorization") authHeader: String)
            : Response<Unit>

    @POST("cafes/hashtag")
    suspend fun getHashTags(@Body prompt: PromptRequest): String

    @POST("auth/signup")
    suspend fun signup(@Body user: UserCreateRequest): UserCreateResponse

    @POST("auth/login")
    suspend fun login(@Body loginData: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") authHeader: String): User

    @POST("lists")
    suspend fun createCafeList(@Body list: ListCreateRequest): CafeList





    @GET("lists/{list_id}")
    suspend fun getCafeListById(@Path("list_id") listId: Int): CafeList

    @GET("lists/{list_id}/get_candidates")
    suspend fun getCafeCandidates(@Path("list_id") listId: Int): List<CafeInfo>

    @PUT("lists/{list_id}/set-default-image")
    suspend fun setDefaultImage(@Body cid: Int): CafeList

    @PUT ("lists/change-name")
    suspend fun changeListName(@Body list: ListChangeNameRequest): CafeList

    @PUT ("lists/{list_id}/delete")
    suspend fun deleteList(@Path("list_id") listId: Int): Unit

    @POST ("lists/{list_id}/add_cafe")
    suspend fun addCafes(@Path("list_id") listId: Int, @Body cid: Int): Unit
}

