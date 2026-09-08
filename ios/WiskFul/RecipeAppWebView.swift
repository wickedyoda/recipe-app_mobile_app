import WebKit
import SwiftUI

struct RecipeAppWebView: UIViewRepresentable {
    let url: URL
    
    func makeUIView(context: Context) -> WKWebView {
        let preferences = WKPreferences()
        preferences.javaScriptEnabled = true
        preferences.javaScriptCanOpenWindowsOnly = false
        
        let config = WKWebViewConfiguration()
        config.preferences = preferences
        config.allowsInlineMediaPlayback = true
        
        // Security: disable file access from web content
        config.preferences.setValue(false, forKey: "allowFileAccessFromFileURLs")
        
        let webView = WKWebView(frame: .zero, configuration: config)
        webView.navigationDelegate = context.coordinator
        webView.uiDelegate = context.coordinator
        webView.allowsBackForwardNavigationGestures = true
        webView.isOpaque = false
        
        return webView
    }
    
    func updateUIView(_ uiView: WKWebView, context: Context) {
        // Only load if the current URL doesn't match (prevents reload on every update)
        if uiView.url != url {
            uiView.load(URLRequest(url: url))
        }
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(url: url)
    }
    
    class Coordinator: NSObject, WKNavigationDelegate, WKUIDelegate {
        let allowedHost: String?
        
        init(url: URL) {
            self.allowedHost = url.host
        }
        
        // Block navigation to external hosts
        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            if let requestURL = navigationAction.request.url {
                let requestHost = requestURL.host
                if let allowed = allowedHost {
                    if requestHost != allowed {
                        AppLogger.shared.w("\(requestURL) (allowed=\(allowed))")
                        decisionHandler(.cancel)
                        return
                    }
                }
            }
            decisionHandler(.allow)
        }
        
        func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
            if let url = webView.url?.absoluteString {
                AppLogger.shared.i("Page loaded: \(url)")
            }
        }
        
        func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
            AppLogger.shared.e("Web resource error: \(error.localizedDescription)")
        }
    }
}