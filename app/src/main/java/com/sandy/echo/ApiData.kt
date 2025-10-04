package com.sandy.echo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class untuk mengirim request ke server (versi stateless).
 */
@JsonClass(generateAdapter = true)
data class ChatRequest(
    @Json(name = "new_message") val newMessage: String,
    @Json(name = "chat_history") val chatHistory: String
)

/**
 * Data class untuk menerima response dari server.
 */
@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "response") val response: String
)