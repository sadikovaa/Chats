package com.kholopova.chats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kholopova.chats.data.ChatClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val api = ChatClient.api
    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    private var messagesJob: Job? = null
    private var requestId = 0

    init {
        loadChannels()
    }

    fun openChannel(name: String) {
        if (_state.value.selectedChannel == name) return
        loadMessages(name)
    }

    fun closeChat() {
        requestId += 1
        messagesJob?.cancel()
        _state.update {
            it.copy(
                selectedChannel = null,
                openImageLink = null,
                messages = emptyList(),
                hasMore = false,
            )
        }
    }

    fun openImage(link: String) {
        if (link.isBlank()) return
        _state.update { it.copy(openImageLink = link) }
    }

    fun closeImage() {
        _state.update { it.copy(openImageLink = null) }
    }

    fun loadOlder() {
        val current = _state.value
        val channel = current.selectedChannel ?: return
        val oldestId = current.messages.minOfOrNull { it.id } ?: return
        if (!current.hasMore || messagesJob?.isActive == true) return
        val id = requestId
        messagesJob = viewModelScope.launch {
            try {
                val page = api.messages(channel, PAGE_SIZE, oldestId, true)
                if (id != requestId) return@launch
                _state.update { state ->
                    if (state.selectedChannel != channel) {
                        state
                    } else {
                        val merged = (state.messages + page)
                            .distinctBy { it.id }
                            .sortedByDescending { it.id }
                        state.copy(
                            messages = merged,
                            hasMore = page.size >= PAGE_SIZE && merged.size > state.messages.size,
                        )
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                return@launch
            }
        }
    }

    private fun loadChannels() {
        viewModelScope.launch {
            try {
                val channels = api.channels()
                _state.update { it.copy(channels = channels) }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                return@launch
            }
        }
    }

    private fun loadMessages(name: String) {
        val id = ++requestId
        messagesJob?.cancel()
        _state.update {
            it.copy(
                selectedChannel = name,
                openImageLink = null,
                messages = emptyList(),
                hasMore = false,
            )
        }
        messagesJob = viewModelScope.launch {
            try {
                val page = api.messages(name, PAGE_SIZE, null, true)
                if (id != requestId) return@launch
                _state.update { state ->
                    if (state.selectedChannel != name) {
                        state
                    } else {
                        state.copy(
                            messages = page.distinctBy { it.id }.sortedByDescending { it.id },
                            hasMore = page.size >= PAGE_SIZE,
                        )
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                return@launch
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
