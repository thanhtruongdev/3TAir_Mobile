package com.example.a3tair.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3tair.utils.Utils.AI_API_KEY
import com.example.chillzone.ai.AiClasses.ChatCompletionRequest
import com.example.chillzone.ai.AiClasses.ChatMessage
import com.example.chillzone.ai.OpenRouterClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiViewModel() : ViewModel() {
    private val _advice = MutableStateFlow<String?>(null)
    val advice: StateFlow<String?> = _advice.asStateFlow()

    fun generateAdvice(airQuality : String) {
        val authHeader = "Bearer $AI_API_KEY"

        Log.i("AIR_QUALITY", "Air quality: $airQuality")

        val request = ChatCompletionRequest(
            model = "google/gemma-3n-e4b-it:free",
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = "Hãy cho tôi một lời khuyên hữu ích để bảo vệ sức khoẻ khi chất lượng không khí đang ở mức $airQuality, viết thành đoạn, ngắn gọn và gần gũi khoảng 30 từ bằng Tiếng Việt Nam, chèn thêm icon hoặc emoji để thêm phần sinh động"
                )
            )
        )
        viewModelScope.launch {
            try {
                val response = OpenRouterClient.service.createChatCompletion(authHeader, request)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        val answer = it.choices[0].message.content
                        _advice.value = answer
                        Log.i("ANSWER", "Answer: $answer")
                    }
                } else {
                    Log.i("ERROR", "No choices in response")
                }
            } catch (e: Exception) {
                Log.i("ERROR", "Error making request: ${e.message}")
            }
        }
    }

}