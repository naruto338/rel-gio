package com.example.ai

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiService {
    @POST("v1/chat/completions")
    fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body body: ChatRequest
    ): Call<ChatResponse>
}

@JsonClass(generateAdapter = true)
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val max_tokens: Int = 150
)

@JsonClass(generateAdapter = true)
data class ChatMessage(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "id") val id: String,
    @Json(name = "choices") val choices: List<Choice>
) {
    @JsonClass(generateAdapter = true)
    data class Choice(
        val index: Int,
        val message: ChatMessage
    )
}
