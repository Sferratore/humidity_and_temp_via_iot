package com.example.iotapp

import java.net.HttpURLConnection
import java.net.URL

object IoTCloudClient {
    @Throws(Exception::class)
    fun getData(urlString: String, token: String): String {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Authorization", "Bearer $token")
        conn.setRequestProperty("Accept", "application/json")

        return conn.inputStream.bufferedReader().use { it.readText() }
    }
}
