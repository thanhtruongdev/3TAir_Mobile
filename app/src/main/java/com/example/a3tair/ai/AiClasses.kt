package com.example.chillzone.ai

class AiClasses {
    data class ChatMessage(
        val role: String,
        val content: String
    )

    data class ChatCompletionRequest(
        val model: String,
        val messages: List<ChatMessage>
    )

    data class ChatChoice(
        val index: Int,
        val message: ChatMessage,
        val finish_reason: String?
    )

    data class ChatCompletionResponse(
        val id: String,
        val created: Long,
        val model: String,
        val choices: List<ChatChoice>,
        val usage: UsageInfo
    )

    data class UsageInfo(
        val prompt_tokens: Int,
        val completion_tokens: Int,
        val total_tokens: Int
    )
}