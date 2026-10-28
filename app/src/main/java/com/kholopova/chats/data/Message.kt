package com.kholopova.chats.data

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") val id: Long,
    @SerializedName("from") val from: String?,
    @SerializedName("data") val data: MessageContent?,
    @SerializedName("time") val time: Long,
)
