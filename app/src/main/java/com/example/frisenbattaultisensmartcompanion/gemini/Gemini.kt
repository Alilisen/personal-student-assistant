package com.example.frisenbattaultisensmartcompanion.gemini


import com.google.ai.client.generativeai.GenerativeModel

object Gemini {
    val generativeModel = GenerativeModel(
        "gemini-1.5-flash",
        "AIzaSyD1XEYgNkwd1-UzRVQ6WXeIyrzMKjiu34s"
    )
    suspend fun generateResponse(input: String): String {
        return try {
            val response = generativeModel.generateContent(input)
            response . text ?: "No response"
        } catch (e: Exception) {
            "Error generating response: ${e.message}"
        }
    }
}