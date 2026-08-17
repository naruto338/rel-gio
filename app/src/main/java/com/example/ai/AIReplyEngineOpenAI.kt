package com.example.ai

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.awaitResponse

class AIReplyEngineOpenAI(private val apiKey: String?) : AIReplyEngine {
    private val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenAiService::class.java)
    }

    override suspend fun generateReply(contextMessages: List<String>, options: AIReplyEngine.Options): String {
        if (apiKey.isNullOrBlank()) {
            return "Erro: OpenAI API key não configurada."
        }
        return withContext(Dispatchers.IO) {
            try {
                val msgs = contextMessages.map { ChatMessage(role = "user", content = it) }
                val req = ChatRequest(model = "gpt-3.5-turbo", messages = msgs)
                val call = service.createChatCompletion("Bearer $apiKey", req)
                val resp = call.awaitResponse()
                if (resp.isSuccessful) {
                    val body = resp.body()
                    val choice = body?.choices?.firstOrNull()
                    choice?.message?.content ?: "Sem resposta."
                } else {
                    Log.e("AIReply", "OpenAI error: ${resp.code()} ${resp.errorBody()?.string()}")
                    "Erro na API: ${resp.code()}"
                }
            } catch (e: Exception) {
                Log.e("AIReply", "Exception: ${e.message}")
                "Erro interno: ${e.message}"
            }
        }
    }
}
