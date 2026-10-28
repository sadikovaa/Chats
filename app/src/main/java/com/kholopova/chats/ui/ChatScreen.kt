package com.kholopova.chats.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kholopova.chats.R

@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val imageOpen = state.openImageLink != null
    val chatOpen = state.selectedChannel != null
    BackHandler(enabled = imageOpen || chatOpen) {
        if (viewModel.state.value.openImageLink != null) {
            viewModel.closeImage()
        } else {
            viewModel.closeChat()
        }
    }

    val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    Box(modifier = modifier.fillMaxSize()) {
        if (landscape) {
            LandscapeBody(
                state = state,
                onSelect = viewModel::openChannel,
                onClose = viewModel::closeChat,
                onLoadOlder = viewModel::loadOlder,
                onOpenImage = viewModel::openImage,
            )
        } else {
            PortraitBody(
                state = state,
                onSelect = viewModel::openChannel,
                onClose = viewModel::closeChat,
                onLoadOlder = viewModel::loadOlder,
                onOpenImage = viewModel::openImage,
            )
        }
        val imageLink = state.openImageLink
        if (imageLink != null) {
            ImageViewer(
                link = imageLink,
                onClose = viewModel::closeImage,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LandscapeBody(
    state: ChatUiState,
    onSelect: (String) -> Unit,
    onClose: () -> Unit,
    onLoadOlder: () -> Unit,
    onOpenImage: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        ChannelList(
            state = state,
            onSelect = onSelect,
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outlineVariant),
        )
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
        ) {
            val channel = state.selectedChannel
            if (channel == null) {
                SelectChatPlaceholder(modifier = Modifier.fillMaxSize())
            } else {
                MessageList(
                    channel = channel,
                    state = state,
                    onClose = onClose,
                    onLoadOlder = onLoadOlder,
                    onOpenImage = onOpenImage,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun PortraitBody(
    state: ChatUiState,
    onSelect: (String) -> Unit,
    onClose: () -> Unit,
    onLoadOlder: () -> Unit,
    onOpenImage: (String) -> Unit,
) {
    val channel = state.selectedChannel
    if (channel == null) {
        ChannelList(
            state = state,
            onSelect = onSelect,
            modifier = Modifier.fillMaxSize(),
        )
    } else {
        MessageList(
            channel = channel,
            state = state,
            onClose = onClose,
            onLoadOlder = onLoadOlder,
            onOpenImage = onOpenImage,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun SelectChatPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.select_chat),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
