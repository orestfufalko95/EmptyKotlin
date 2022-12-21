package org.example

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

data class TestModel(
    val id: Int,
    val description: String
)

class Orest {
    fun echo(word: String): String {

        val jsonStr = "{\"id\":1,\"description\":\"Test\"}"

        var gson = Gson()
        var jsonString = gson.toJson(TestModel(1, "Test"))

        var jsonString2 = """{"id":1,"description":"Test"}""";
        var testModel: TestModel = gson.fromJson(jsonString2, TestModel::class.java)

        run()

        return testModel.description + word
    }

    private val client = OkHttpClient()

    fun run() {
        val request = Request.Builder()
            .url("https://publicobject.com/helloworld.txt")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
            println("request finished")
        }
    }
}