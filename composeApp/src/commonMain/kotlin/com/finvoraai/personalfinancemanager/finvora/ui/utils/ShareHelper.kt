package com.finvoraai.personalfinancemanager.finvora.ui.utils

interface ShareHelper {
    fun shareText(text: String, title: String = "Share FinvoraAI")
    fun openEmail(recipient: String, subject: String, body: String)
}
