package com.whiskful.webview

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppLog {
    private const val TAG = "WiskFul"
    private const val LOG_FILE = "wiskful-app.log"
    private const val MAX_LOG_SIZE = 10 * 1024 * 1024 // 10 MB
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    private var logFile: File? = null

    fun init(context: Context) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (dir != null) {
            if (!dir.exists()) dir.mkdirs()
            logFile = File(dir, LOG_FILE)
            trimIfNeeded()
        }
    }

    fun d(message: String) {
        Log.d(TAG, message)
        write("DEBUG", message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
        write("INFO", message)
    }

    fun w(message: String, throwable: Throwable? = null) {
        Log.w(TAG, message, throwable)
        write("WARN", message, throwable)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
        write("ERROR", message, throwable)
    }

    fun getLogFile(): File? = logFile

    private fun write(level: String, message: String, throwable: Throwable? = null) {
        val file = logFile ?: return
        try {
            FileWriter(file, true).use { writer ->
                val timestamp = dateFormat.format(Date())
                var line = "$timestamp $level: $message"
                if (throwable != null) {
                    line += "\n${Log.getStackTraceString(throwable)}"
                }
                writer.appendln(line)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write log", e)
        }
    }

    private fun trimIfNeeded() {
        val file = logFile ?: return
        try {
            if (file.exists() && file.length() > MAX_LOG_SIZE) {
                val text = file.readText()
                val cutoff = text.length / 2
                val half = text.substring(cutoff)
                file.writeText(half)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to trim log", e)
        }
    }
}
