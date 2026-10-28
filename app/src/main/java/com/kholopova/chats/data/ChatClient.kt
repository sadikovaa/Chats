package com.kholopova.chats.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatClient {
    private const val BASE_URL = "https://faerytea.name/"

    val api: ChatApi by lazy {
        val gson = GsonBuilder()
            .registerTypeAdapter(MessageContent::class.java, MessageContentAdapter())
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ChatApi::class.java)
    }
}
