package com.kholopova.chats.ui

import com.kholopova.chats.data.Message

data class ChatUiState(
    val channels: List<String> = emptyList(),
    val selectedChannel: String? = null,
    val messages: List<Message> = emptyList(),
    val hasMore: Boolean = false,
    val openImageLink: String? = null,
)
