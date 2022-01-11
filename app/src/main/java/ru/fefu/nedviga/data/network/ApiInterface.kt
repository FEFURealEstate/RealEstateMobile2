package ru.fefu.nedviga.data.network

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.fefu.nedviga.data.models.UserToken
import ru.fefu.nedviga.data.models.User

interface ApiInterface {
//    @GET("api/user/profile")
//    suspend fun getProfile(): User

    @POST("login")
    suspend fun login(
        @Query("username") login: String,
        @Query("password") password: String,
    ): UserToken

    @POST("logout")
    suspend fun logout(): Unit
}