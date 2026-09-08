package com.whiskful.webview.network

import android.util.Base64
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object SmtpConfig {
    const val SMTP_HOST = "smtp.migadu.com"
    const val SMTP_PORT = 465  // TLS

    fun getPassword(): String {
        return BuildConfig.SMTP_PASSWORD
    }

    fun getUsername(): String {
        return BuildConfig.SMTP_USER
    }

    fun sendSupportLog(
        subject: String,
        body: String,
        logContent: String? = null
    ): Boolean {
        return try {
            val props = Properties().apply {
                put("mail.smtp.host", SMTP_HOST)
                put("mail.smtp.port", SMTP_PORT.toString())
                put("mail.smtp.ssl.enable", "true")
                put("mail.smtp.ssl.protocols", "TLSv1.3")
                put("mail.smtp.auth", "true")
                put("mail.smtp.ssl.checkserveridentity", "true")
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(getUsername(), getPassword())
                }
            })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(getUsername()))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(getUsername()))
                this.subject = subject
                setText(if (!logContent.isNullOrEmpty()) {
                    "$body\n\n--- Log Content ---\n$logContent"
                } else {
                    body
                })
            }

            Transport.send(message)
            true
        } catch (e: Exception) {
            AppLog.e("SMTP send failed: ${e.message}")
            false
        }
    }
}
