# IoT Secure Temperature & Humidity Monitoring System

## Overview
This project is an IoT-based system for monitoring temperature and humidity securely. The system consists of an **Arduino R4** microcontroller connected to a **DHT sensor** for measuring environmental parameters. The data is sent securely to the cloud and retrieved using an **Android application**, which decrypts and displays it.

## Features
- **Arduino R4** collects temperature and humidity data from a **DHT sensor**.
- Secure transmission of data to the **cloud**.
- **Android app (written in Kotlin)** fetches and decrypts the data.
- AES encryption ensures data confidentiality.
- User-friendly UI with real-time updates.

## Circuit Design
### Components Used
- **Arduino R4**
- **DHT11 or DHT22 Sensor** (Temperature & Humidity)
- **Resistors & Jumper Wires**
- **Breadboard**

### Wiring
| Arduino R4 Pin | DHT Sensor Pin |
|--------------|---------------|
| 5V          | VCC           |
| GND         | GND           |
| D2          | Data          |

A **10kΩ pull-up resistor** is recommended between the **Data** pin and **VCC**.

## Software Structure
### 1. Arduino Firmware
- Reads temperature & humidity from the DHT sensor.
- Encrypts the data before transmission.
- Sends encrypted data to the **cloud**.

### 2. Cloud Storage
- The data is securely stored in **Arduino Cloud** (or a similar cloud service).

### 3. Android App
- Retrieves encrypted data from the cloud.
- Decrypts data using **AES** (implemented in `AESUtils.kt`).
- Displays temperature and humidity values.

## Setup Instructions
### Arduino R4
1. Install the **DHT library** in the Arduino IDE.
2. Upload the provided **Arduino R4 firmware**.
3. Configure **Wi-Fi credentials** and **cloud endpoint**.

### Android App
1. Open the **HumidityTempAndroid** project in **Android Studio**.
2. Sync dependencies and build the project.
3. Deploy on Android device.

## Security Considerations
- **AES Encryption** ensures data privacy.
- Secure communication protocols like **HTTPS/MQTT-TLS**.
- **Access control mechanisms** for cloud storage.

