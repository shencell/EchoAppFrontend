package com.sandy.echo

import java.util.UUID

/**
 * Merepresentasikan satu objek pesan di dalam UI.
 * @param id ID unik untuk setiap pesan, berguna untuk performa di LazyColumn.
 * @param text Isi dari pesan.
 * @param isUser Bernilai true jika pesan dari pengguna, false jika dari AI.
 */
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val id: String = UUID.randomUUID().toString() // Membuat ID unik otomatis
)