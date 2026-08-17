package com.example.voice

import android.content.Context

interface VoiceManager {
    fun startListening(context: Context, onResult: (text: String) -> Unit, onError: (Throwable) -> Unit)
    fun stopListening()
}
