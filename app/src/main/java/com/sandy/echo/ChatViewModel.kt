package com.sandy.echo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Menambahkan pesan sambutan awal saat ViewModel dibuat
        _uiState.update {
            it.copy(
                messages = listOf(ChatMessage(text = "Halo! Aku Echo, ada yang bisa aku bantu?", isUser = false))
            )
        }
    }

    // Dipanggil oleh UI setiap kali pengguna mengetik
    fun onInputTextChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    // Dipanggil oleh UI saat tombol kirim ditekan
    fun sendMessage() {
        val currentState = _uiState.value
        if (currentState.inputText.isBlank() || currentState.isLoading) return

        val userMessage = ChatMessage(text = currentState.inputText, isUser = true)

        // Update UI secara optimis: langsung tampilkan pesan pengguna & loading
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                inputText = "" // Kosongkan input
            )
        }

        viewModelScope.launch {
            try {
                // Siapkan riwayat chat dari state saat ini
                val history = currentState.messages
                    .joinToString("\n") { message -> if (message.isUser) "User: ${message.text}" else "AI: ${message.text}" }

                val request = ChatRequest(
                    newMessage = userMessage.text,
                    chatHistory = history
                )

                val response = RetrofitClient.instance.postMessage(request)

                if (response.isSuccessful && response.body() != null) {
                    val aiMessage = ChatMessage(text = response.body()!!.response, isUser = false)
                    _uiState.update { it.copy(messages = it.messages + aiMessage) }
                } else {
                    val errorMsg = ChatMessage(text = "Gagal mendapatkan respons dari server.", isUser = false)
                    _uiState.update { it.copy(messages = it.messages + errorMsg) }
                }
            } catch (e: Exception) {
                val errorMsg = ChatMessage(text = "Tidak dapat terhubung ke server.", isUser = false)
                _uiState.update { it.copy(messages = it.messages + errorMsg) }
                Log.e("ChatViewModel", "Exception: ${e.message}")
            } finally {
                // Selalu hentikan loading setelah selesai
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}