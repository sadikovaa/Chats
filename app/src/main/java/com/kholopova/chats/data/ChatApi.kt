package com.kholopova.chats.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("channels")
    suspend fun channels(): List<String>

    @GET("channel/{name}")
    suspend fun messages(
        @Path("name") name: String,
        @Query("limit") limit: Int,
        @Query("lastKnownId") lastKnownId: Long?,
        @Query("reverse") reverse: Boolean,
    ): List<Message>
}
