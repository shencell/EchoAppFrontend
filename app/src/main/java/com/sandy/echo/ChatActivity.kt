package com.sandy.echo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sandy.echo.ui.theme.EchoAppTheme // Ganti dengan nama tema Compose Anda

class ChatActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            EchoAppTheme {
                // Mengambil state dari ViewModel
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                Surface(modifier = Modifier.fillMaxSize()) {
                    ChatScreen(
                        // Teruskan state dan fungsi dari ViewModel ke UI
                        uiState = uiState,
                        onSendMessage = viewModel::sendMessage,
                        onInputTextChanged = viewModel::onInputTextChanged
                    )
                }
            }
        }
    }
}