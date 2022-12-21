package org.example

import com.google.gson.Gson

data class TestModel(
    val id: Int,
    val description: String
)

class Orest {
    fun echo(word: String): String {
        val jsonStr = "{\"id\":1,\"description\":\"Test\"}"

        var gson = Gson()
        var jsonString = gson.toJson(TestModel(1,"Test"))

        var jsonString2 = """{"id":1,"description":"Test"}""";
        var testModel: TestModel = gson.fromJson(jsonString2, TestModel::class.java)
        return testModel.description + word
    }
}