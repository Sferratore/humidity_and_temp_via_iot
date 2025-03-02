package com.example.iotapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var tempTextView: TextView
    private lateinit var humTextView: TextView

    companion object {
        private const val CLIENT_ID = "8oUXcZhplNfUpIVs2R8MTrpBqGUm1nei"
        private const val CLIENT_SECRET = "wVDELZtOjteLeFLs3xF9YKHejbfd9crsduZTBSGR189wkqwpyRjqVW5AtsU2iBrd"
        private const val ARDUINO_CLOUD_URL_TEMP = "https://api2.arduino.cc/iot/v2/things/e5ee2c01-f522-4240-9725-2e9fe77fa5c5/properties/74d35c79-226e-482a-af11-afba3b6dc774"
        private const val ARDUINO_CLOUD_URL_HUM = "https://api2.arduino.cc/iot/v2/things/e5ee2c01-f522-4240-9725-2e9fe77fa5c5/properties/78ecdac7-3f12-4839-97f0-f065f005f174"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempTextView = findViewById(R.id.tempTextView)
        humTextView = findViewById(R.id.humTextView)

        fetchAccessToken { accessToken ->
            if (accessToken != null) {
                fetchIoTData(accessToken)
            } else {
                tempTextView.text = "Errore nel recupero token"
                humTextView.text = "Errore nel recupero token"
            }
        }
    }

    private fun fetchAccessToken(callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val tokenUrl = "https://api2.arduino.cc/iot/v1/clients/token"
            val postData = "grant_type=client_credentials&client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&audience=https://api2.arduino.cc/iot"
            val url = URL(tokenUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.doOutput = true

            withContext(Dispatchers.IO) {
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(postData)
                writer.flush()
                writer.close()
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                val accessToken = jsonResponse.getString("access_token")
                withContext(Dispatchers.Main) {
                    callback(accessToken)
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
            conn.disconnect()
        }
    }

    private fun fetchIoTData(accessToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val decryptedTemp = fetchAndDecryptData(ARDUINO_CLOUD_URL_TEMP, accessToken)
                val decryptedHum = fetchAndDecryptData(ARDUINO_CLOUD_URL_HUM, accessToken)

                withContext(Dispatchers.Main) {
                    tempTextView.text = "Temperatura: $decryptedTemp°C"
                    humTextView.text = "Umidità: $decryptedHum%"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    tempTextView.text = "Errore nel recupero dati"
                    humTextView.text = "Errore nel recupero dati"
                }
            }
        }
    }

    private fun fetchAndDecryptData(urlString: String, accessToken: String): String? {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Authorization", "Bearer $accessToken")
        conn.setRequestProperty("Accept", "application/json")

        var decryptedValue: String? = null
        try {
            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("Errore nella richiesta API: ${conn.responseCode} ${conn.responseMessage}")
            }

            val jsonResponse = conn.inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonResponse)
            val encryptedValue = jsonObject.getString("last_value")
            //decryptedValue = AESUtils.decrypt(encryptedValue)
            decryptedValue = encryptedValue
        } finally {
            conn.disconnect()
        }
        return decryptedValue
    }
}
