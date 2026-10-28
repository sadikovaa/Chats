package com.kholopova.chats.data

sealed interface MessageContent {
    data class Text(val text: String) : MessageContent

    data class Image(val link: String) : MessageContent
}
