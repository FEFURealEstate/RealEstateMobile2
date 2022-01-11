package ru.fefu.nedviga.data.network

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.data.models.ToDoTask
import ru.fefu.nedviga.data.models.UserToken
import ru.fefu.nedviga.data.models.User

interface ApiInterface {
//    @GET("api/user/profile")
//    suspend fun getProfile(): User

    @POST("login")
    suspend fun login(
        @Query("username") login: String,
        @Query("password") password: String,
    ): User

    @POST("logout")
    suspend fun logout(): Unit

    @GET("events")
    suspend fun getEvents(
        @Query("agentId") agentId: Int
    ): List<ToDoTask>

    @DELETE("eventDel")
    suspend fun deleteEvent(
        @Query("uuid") uuid: String
    ): Response<Unit>

    @POST("event")
    suspend fun createEvent(
        @Query("agentId") agentId: Int,
        @Query("datetime") datetime: Int,
        @Query("date") date: String,
        @Query("duration") duration: Int,
        @Query("type") type: String,
        @Query("comment") comment: String
    ): ToDoTask

    @POST("eventUpd")
    suspend fun updateEvent(
        @Query("uuid") uuid: String,
        @Query("agentId") agentId: Int,
        @Query("datetime") datetime: Int,
        @Query("date") date: String,
        @Query("duration") duration: Int,
        @Query("type") type: String,
        @Query("comment") comment: String
    ): Response<Unit>

    @DELETE("eventDelAll")
    suspend fun deleteAllEvents(
        @Query("agentId") agentId: Int
    ): Response<Unit>
}