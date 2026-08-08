package com.whiskful.webview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat

private const val PREFS_NAME = "host_prefs"
private const val KEY_HOST = "host_name"

class MainActivity : AppCompatActivity() {
    private lateinit var urlInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.parseColor("#0f1115")
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedHost = prefs.getString(KEY_HOST, "").orEmpty()

        if (savedHost.isNotEmpty()) {
            launchWebView(savedHost)
        } else {
            showHostEntry(prefs)
        }
    }

    private fun showHostEntry(prefs: android.content.SharedPreferences) {
        urlInput = EditText(this).apply {
            hint = "Enter server URL (e.g. https://192.168.1.100:3000)"
            setText(prefs.getString(KEY_HOST, ""))
        }

        AlertDialog.Builder(this)
            .setTitle("Enter Server Address")
            .setView(urlInput)
            .setPositiveButton("Connect") { _, _ ->
                val host = urlInput.text.toString().trim()
                if (validateHost(host)) {
                    prefs.edit().putString(KEY_HOST, host).apply()
                    launchWebView(host)
                } else {
                    Toast.makeText(this, "Invalid URL. Use https://host:port", Toast.LENGTH_LONG).show()
                    showHostEntry(prefs)
                }
            }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun validateHost(host: String): Boolean {
        val url = if (host.startsWith("http://") || host.startsWith("https://")) {
            host
        } else {
            "https://$host"
        }
        val parsedUri = Uri.parse(url)
        if (parsedUri.scheme == null || parsedUri.host == null) return false
        if (parsedUri.scheme != "https") return false
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun launchWebView(host: String) {
        val webView = WebView(this)
        setContentView(webView)

        val url = if (host.startsWith("http://") || host.startsWith("https://")) host else "https://$host"
        val ws = webView.settings
        ws.javaScriptEnabled = true
        ws.domStorageEnabled = true
        ws.loadWithOverviewMode = true
        ws.useWideViewPort = true
        ws.allowFileAccess = false
        ws.allowFileAccessFromFileURLs = false
        ws.allowUniversalAccessFromFileURLs = false
        ws.allowContentAccess = false

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val requestUri = request.url
                val requestHost = requestUri?.host
                val allowedHost = Uri.parse(url).host
                return requestHost != allowedHost
            }
        }

        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(url)
    }
}
