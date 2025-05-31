package com.example.a3tair.interfaces

import com.example.chillzone.ai.AiClasses.ChatCompletionRequest
import com.example.chillzone.ai.AiClasses.ChatCompletionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenRouterApi {
    @POST("api/v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>
}