#include "info.h"
#include <DHT.h>
#include <Crypto.h>
#include <AES.h>

#define DHTPIN A1  
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

String temperatura;
String umidita;

void initProperties() {
  ArduinoCloud.addProperty(temperatura, READ, ON_CHANGE, NULL);
  ArduinoCloud.addProperty(umidita, READ, ON_CHANGE, NULL);
}

// AES-128 Encryption
void encryptAES(byte* plaintext, byte* ciphertext) {
  AES128 aes;
  aes.setKey(aes_key, 16);
  aes.encryptBlock(ciphertext, plaintext);
}

void setup() {
  Serial.begin(115200);
  dht.begin();
  WiFi.begin(ssid, password);

  int attempts = 0;
  Serial.println(WiFi.status());
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    attempts++;
    if (attempts > 20) { 
      NVIC_SystemReset();
    }
  }

  initProperties();
  ArduinoCloud.begin(ArduinoIoTPreferredConnection);
}

void loop() {
  ArduinoCloud.update();

  float temp = dht.readTemperature();
  float hum = dht.readHumidity();

  if (!isnan(temp) && !isnan(hum)) {
    byte plaintext_temp[16] = {0};
    byte plaintext_hum[16] = {0};
    byte ciphertext_temp[16] = {0};
    byte ciphertext_hum[16] = {0};

    snprintf((char*)plaintext_temp, sizeof(plaintext_temp), "%02d", (int)temp);
    snprintf((char*)plaintext_hum, sizeof(plaintext_hum), "%02d", (int)hum);

    encryptAES(plaintext_temp, ciphertext_temp);
    encryptAES(plaintext_hum, ciphertext_hum);

    char hexTemp[33] = {0};
    char hexHum[33] = {0};

    for (int i = 0; i < 16; i++) {
      sprintf(hexTemp + i * 2, "%02X", ciphertext_temp[i]);
      sprintf(hexHum + i * 2, "%02X", ciphertext_hum[i]);
    }

    temperatura = String(hexTemp);
    umidita = String(hexHum);
  }

  delay(5000);
}
