package com.sandy.echo

import androidx.compose.foundation.Image // <-- TAMBAHKAN IMPORT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape // <-- TAMBAHKAN IMPORT (Opsional, untuk membuat gambar bulat)
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // <-- TAMBAHKAN IMPORT (Opsional, untuk membuat gambar bulat)
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // <-- TAMBAHKAN IMPORT
import androidx.compose.ui.res.painterResource // <-- TAMBAHKAN IMPORT
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- PERUBAHAN DI BAWAH ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(text = "Echo")
        },
        modifier = modifier,
        // --- PENAMBAHAN BARU ---
        // Menambahkan ikon/gambar di sisi kiri TopAppBar
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.icon), // Ganti dengan ID resource gambar Anda
                contentDescription = "Gambar Profil",
                contentScale = ContentScale.Crop, // Agar gambar terpotong rapi, bukan gepeng
                modifier = Modifier
                    .padding(start = 8.dp) // Beri sedikit jarak dari tepi kiri
                    .size(40.dp) // Atur ukuran gambar
                    .clip(CircleShape) // Membuat gambar menjadi bulat
            )
        },
        // --- AKHIR PENAMBAHAN BARU ---
        colors = TopAppBarDefaults.topAppBarColors(
            // Mengatur warna latar belakang TopAppBar
            containerColor = MaterialTheme.colorScheme.primary,
            // Mengatur warna teks judul
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            // Mengatur warna navigation icon (jika berupa tintable vector)
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    uiState: ChatUiState,
    onSendMessage: () -> Unit,
    onInputTextChanged: (String) -> Unit
) {
    val listState = rememberLazyListState()

    // Efek untuk auto-scroll ke pesan terbaru
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(index = uiState.messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(), // Menangani keyboard secara otomatis
        topBar = {
            EchoTopAppBar() // Tidak perlu ada perubahan di sini
        },
        bottomBar = {
            ChatInputBar(
                text = uiState.inputText,
                onTextChanged = onInputTextChanged,
                isLoading = uiState.isLoading,
                onSendClicked = onSendMessage
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // paddingValues sekarang sudah termasuk tinggi TopAppBar dan BottomAppBar
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = uiState.messages,
                key = { message -> message.id } // Kunci unik untuk performa
            ) { message ->
                ChatMessageItem(message = message)
            }
        }
    }
}

// ... (Sisa kode tidak perlu diubah)

@Composable
fun ChatInputBar(
    text: String,
    onTextChanged: (String) -> Unit,
    isLoading: Boolean,
    onSendClicked: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ketik pesanmu...") },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else {
                IconButton(
                    onClick = onSendClicked,
                    enabled = text.isNotBlank() && !isLoading
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim")
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    val bubbleColor = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bubbleColor,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val sampleUiState = ChatUiState(
        messages = listOf(
            ChatMessage(text = "Halo! Ini pesan dari AI.", isUser = false),
            ChatMessage(text = "Halo juga! Ini pesanku.", isUser = true)
        ),
        inputText = "Pesan baru",
        isLoading = false
    )
    MaterialTheme {
        ChatScreen(
            uiState = sampleUiState,
            onSendMessage = {},
            onInputTextChanged = {}
        )
    }
}
