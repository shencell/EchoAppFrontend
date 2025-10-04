package com.sandy.echo

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("chat")
    suspend fun postMessage(@Body requestBody: ChatRequest): Response<ChatResponse>
}