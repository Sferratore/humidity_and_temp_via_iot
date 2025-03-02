#ifndef INFO_H
#define INFO_H

#include <WiFiS3.h>
#include <ArduinoIoTCloud.h>
#include <Arduino_ConnectionHandler.h>

const char* ssid = "TIM-ssid_replace";
const char* password = "pass_replace";

WiFiConnectionHandler ArduinoIoTPreferredConnection(ssid, password);

// Chiave AES-128
const byte aes_key[16] = { 0xA3, 0x2B, 0x5C, 0x3D, 0x4F, 0x12, 0xE6, 0x7A, 
                           0x8B, 0x6C, 0x9D, 0x1F, 0xB0, 0xA1, 0xC2, 0xD3 };

#endif
