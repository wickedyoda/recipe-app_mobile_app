package com.whiskful.webview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import java.io.File
import java.io.FileWriter

private const val PREFS_NAME = "host_prefs"
private const val KEY_HOST = "host_name"
private const val TAG = "WiskFul"

class MainActivity : AppCompatActivity() {
    private lateinit var urlInput: EditText
    private var rootView: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.parseColor("#0f1115")
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        // Use full-screen edge-to-edge so the WebView fills behind the status bar,
        // then apply padding in launchWebView so content sits BELOW the bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        AppLog.init(this)
        AppLog.i("App started")

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
        AppLog.i("Launching WebView for host=$host")
        val webView = WebView(this)
        rootView = FrameLayout(this)
        rootView?.addView(webView)
        setContentView(rootView)

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
                val override = requestHost != allowedHost
                if (override) {
                    AppLog.w("Blocked navigation: ${requestUri} (allowed=$allowedHost)")
                }
                return override
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                AppLog.i("Page loaded: $url")
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: android.webkit.WebResourceError) {
                super.onReceivedError(view, request, error)
                AppLog.e("Web resource error: ${error.description} url=${request.url}")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                val msg = "${consoleMessage.sourceId()}:${consoleMessage.lineNumber()} ${consoleMessage.message()}"
                when (consoleMessage.messageLevel()) {
                    android.webkit.ConsoleMessage.MessageLevel.ERROR -> AppLog.e(msg)
                    android.webkit.ConsoleMessage.MessageLevel.WARNING -> AppLog.w(msg)
                    else -> AppLog.d(msg)
                }
                return true
            }
        }

        rootView?.let { attachLogFab(it) }
        webView.loadUrl(url)
    }

    private fun attachLogFab(container: FrameLayout) {
        val fab = android.widget.Button(this).apply {
            text = "Logs"
            alpha = 0.85f
            val px = (16 * resources.displayMetrics.density).toInt()
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = px
                bottomMargin = px
                gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
            }
            layoutParams = params
            setPadding(px, (px / 2), px, (px / 2))
            setOnClickListener {
                shareLog()
            }
        }
        container.addView(fab)
    }

    private fun shareLog() {
        try {
            val logFile = AppLog.getLogFile()
            if (logFile == null || !logFile.exists()) {
                Toast.makeText(this, "No log file yet", Toast.LENGTH_SHORT).show()
                return
            }
            val uri = androidx.core.content.FileProvider.getUriForFile(
                this,
                "$packageName.fileprovider",
                logFile
            )
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(android.content.Intent.createChooser(intent, "Share WiskFul logs"))
            AppLog.i("Shared log file=${logFile.absolutePath} size=${logFile.length()}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share logs", e)
            Toast.makeText(this, "Failed to share logs", Toast.LENGTH_SHORT).show()
        }
    }
}
