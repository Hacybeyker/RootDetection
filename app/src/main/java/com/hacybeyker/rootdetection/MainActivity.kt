package com.hacybeyker.rootdetection

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.scottyab.rootbeer.RootBeer
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

const val TAG = "HERE"

class MainActivity : AppCompatActivity() {

    private val binaryPaths = arrayOf(
        "/data/local/",
        "/data/local/bin/",
        "/data/local/xbin/",
        "/sbin/",
        "/su/bin/",
        "/system/bin/",
        "/system/bin/.ext/",
        "/system/bin/failsafe/",
        "/system/sd/xbin/",
        "/system/usr/we-need-root/",
        "/system/xbin/",
        "/system/app/Superuser.apk",
        "/cache",
        "/data",
        "/dev"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        detectTestKeys()

        checkForSuBinary()
        checkForBusyBoxBinary()
        val value = checkSuExists()
        Log.d(TAG, "checkSuExists: $value")


        val rootBeer = RootBeer(this)
        if (rootBeer.isRooted) {
            Log.d(TAG, "IS-ROOT")
        } else {
            Log.d(TAG, "IS NOT - ROOT")
        }
    }

    private fun detectTestKeys(): Boolean {
        val buildTags = android.os.Build.TAGS
        Log.d(TAG, "BOARD: ${android.os.Build.BOARD}")
        Log.d(TAG, "BOOTLOADER: ${android.os.Build.BOOTLOADER}")
        Log.d(TAG, "BRAND: ${android.os.Build.BRAND}")
        Log.d(TAG, "DEVICE: ${android.os.Build.DEVICE}")
        Log.d(TAG, "DISPLAY: ${android.os.Build.DISPLAY}")
        Log.d(TAG, "FINGERPRINT: ${android.os.Build.FINGERPRINT}")
        Log.d(TAG, "HARDWARE: ${android.os.Build.HARDWARE}")
        Log.d(TAG, "HOST: ${android.os.Build.HOST}")
        Log.d(TAG, "ID: ${android.os.Build.ID}")
        Log.d(TAG, "MANUFACTURER: ${android.os.Build.MANUFACTURER}")
        Log.d(TAG, "MODEL: ${android.os.Build.MODEL}")
        Log.d(TAG, "PRODUCT: ${android.os.Build.PRODUCT}")
        Log.d(TAG, "TAGS: ${android.os.Build.TAGS}")
        Log.d(TAG, "TIME: ${android.os.Build.TIME}")
        Log.d(TAG, "TYPE: ${android.os.Build.TYPE}")
        Log.d(TAG, "UNKNOWN: ${android.os.Build.UNKNOWN}")
        Log.d(TAG, "USER: ${android.os.Build.USER}")

        Log.d(TAG, "buildTags: $buildTags")
        buildTags.contains("test-keys")

        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkForSuBinary(): Boolean {
        val value = checkForBinary("su")
        Log.d(TAG, "checkForSuBinary: $value")
        return value
    }

    private fun checkForBusyBoxBinary(): Boolean {
        val value = checkForBinary("busybox")
        Log.d(TAG, "checkForBusyBoxBinary: $value")
        return value
    }

    private fun checkForBinary(fileName: String): Boolean {
        for (path in binaryPaths) {
            val file = File(path, fileName)
            val fileExists = file.exists()
            if (fileExists)
                return true
        }
        return false
    }

    private fun checkSuExists(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system /xbin/which", "su"))
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            val line = bufferedReader.readLine()
            process.destroy()
            line != null
        } catch (e: Exception) {
            process?.destroy()
            false
        }
    }
}
