package com.finvoraai.personalfinancemanager

import com.finvoraai.personalfinancemanager.finvora.ui.utils.ShareHelper
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow

class IosShareHelper : ShareHelper {
    override fun shareText(text: String, title: String) {
        val window = UIApplication.sharedApplication.keyWindow
            ?: UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow
        val rootViewController = window?.rootViewController
        val activityViewController = UIActivityViewController(listOf(text), null)
        rootViewController?.presentViewController(activityViewController, true, null)
    }

    override fun openEmail(recipient: String, subject: String, body: String) {
        val encodedSubject = subject.replace(" ", "%20")
        val encodedBody = body.replace(" ", "%20").replace("\n", "%0A")
        val urlString = "mailto:$recipient?subject=$encodedSubject&body=$encodedBody"
        val url = platform.Foundation.NSURL(string = urlString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}
