package com.example.ai

interface AIReplyEngine {
    data class Options(
        val style: String = "normal", // curta, detalhada, formal, casual, etc
        val personalized: Boolean = false
    )

    suspend fun generateReply(contextMessages: List<String>, options: Options): String
}
