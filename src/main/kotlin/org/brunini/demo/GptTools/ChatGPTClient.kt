package org.brunini.demo.GptTools
import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class ChatGPTClient(private val apiKey: String) {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun evaluateAccessibility(htmlOrCss: String, callback: (String) -> Unit) {
        val requestBody = mapOf(
            "model" to "gpt-3.5-turbo",
            "messages" to listOf(
                mapOf("role" to "system", "content" to "Você é um especialista em acessibilidade da WCAG 2.2."),
                mapOf("role" to "user", "content" to "Avalie o seguinte código HTML/CSS de acordo com as diretrizes WCAG 2.2 e forneça problemas categorizados e ações corretivas: $htmlOrCss")
            )
        )

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(RequestBody.create("application/json".toMediaType(), gson.toJson(requestBody)))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Erro ao conectar com o ChatGPT: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonResponse = gson.fromJson(response.body?.string(), JsonObject::class.java)
                    val feedback = jsonResponse["choices"].asJsonArray[0].asJsonObject["message"].asJsonObject["content"].asString
                    callback(feedback)
                } else {
                    callback("Falha na análise do ChatGPT: ${response.message}")
                }
            }
        })
    }
}
