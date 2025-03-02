package com.example.iotapp

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object AESUtils {
    private const val ALGORITHM = "AES/ECB/PKCS5Padding"
    private val KEY = byteArrayOf(
        0xA3.toByte(), 0x2B.toByte(), 0x5C.toByte(), 0x3D.toByte(),
        0x4F.toByte(), 0x12.toByte(), 0xE6.toByte(), 0x7A.toByte(),
        0x8B.toByte(), 0x6C.toByte(), 0x9D.toByte(), 0x1F.toByte(),
        0xB0.toByte(), 0xA1.toByte(), 0xC2.toByte(), 0xD3.toByte()
    )

    fun decrypt(encryptedData: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val secretKey = SecretKeySpec(KEY, "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedBytes = cipher.doFinal(hexStringToByteArray(encryptedData))
        return String(decryptedBytes).trim()
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
        }
        return data
    }
}
