package com.sandy.echo

/**
 * Merepresentasikan semua state yang dibutuhkan oleh layar chat.
 */
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false
)