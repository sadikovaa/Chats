package com.kholopova.chats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.kholopova.chats.ui.ChatRoute
import com.kholopova.chats.ui.theme.ChatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatRoute(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
