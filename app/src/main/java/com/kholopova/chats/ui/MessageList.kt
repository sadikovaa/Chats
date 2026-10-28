package com.kholopova.chats.ui

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kholopova.chats.R
import com.kholopova.chats.data.ImageUrls
import com.kholopova.chats.data.Message
import com.kholopova.chats.data.MessageContent

@Composable
fun MessageList(
    channel: String,
    state: ChatUiState,
    onClose: () -> Unit,
    onLoadOlder: () -> Unit,
    onOpenImage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Surface(tonalElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onClose) {
                    Text(text = stringResource(R.string.back))
                }
                Text(
                    text = channel,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        MessageRows(
            state = state,
            onLoadOlder = onLoadOlder,
            onOpenImage = onOpenImage,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun MessageRows(
    state: ChatUiState,
    onLoadOlder: () -> Unit,
    onOpenImage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = state.messages, key = { it.id }) { message ->
            MessageCard(message = message, onOpenImage = onOpenImage)
        }
        if (state.hasMore) {
            item(key = "older") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    TextButton(onClick = onLoadOlder) {
                        Text(text = stringResource(R.string.load_older))
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageCard(
    message: Message,
    onOpenImage: (String) -> Unit,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val author = message.from
            if (!author.isNullOrBlank()) {
                Text(
                    text = author,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            MessageBody(content = message.data, onOpenImage = onOpenImage)
            Text(
                text = DateUtils.formatDateTime(
                    context,
                    message.time,
                    DateUtils.FORMAT_SHOW_DATE or
                        DateUtils.FORMAT_SHOW_TIME or
                        DateUtils.FORMAT_ABBREV_MONTH,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
private fun MessageBody(
    content: MessageContent?,
    onOpenImage: (String) -> Unit,
) {
    when (content) {
        is MessageContent.Text -> Text(
            text = content.text,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodyLarge,
        )

        is MessageContent.Image -> {
            if (content.link.isNotBlank()) {
                AsyncImage(
                    model = ImageUrls.thumbnail(content.link),
                    contentDescription = stringResource(R.string.image_message),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable { onOpenImage(content.link) },
                    contentScale = ContentScale.Fit,
                )
            }
        }

        null -> Unit
    }
}
