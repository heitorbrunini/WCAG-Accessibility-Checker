class ChatGPTClient {
    private val settings = AccessibilityCheckerSettings.getInstance().state
    private val client = OkHttpClient()
    private val gson = Gson()

    fun evaluateAccessibility(htmlOrCss: String, callback: (String) -> Unit) {
        val requestBody = mapOf(
            "model" to "gpt-3.5-turbo",
            "temperature" to settings.temperature,
            "messages" to listOf(
                mapOf("role" to "system", "content" to "Você é um especialista em acessibilidade da WCAG 2.2."),
                mapOf(
                    "role" to "user",
                    "content" to "Avalie o seguinte código HTML/CSS de acordo com as diretrizes WCAG 2.2, considerando a versão HTML ${settings.htmlVersion} e CSS ${settings.cssVersion}: $htmlOrCss"
                )
            )
        )

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${settings.apiKey}")
            .post(RequestBody.create(MediaType.get("application/json"), gson.toJson(requestBody)))
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
