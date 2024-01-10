package com.example.newsapiproject


import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView: WebView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true

        // Set cache mode to LOAD_CACHE_ELSE_NETWORK
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        val url = intent.getStringExtra("URL")
        if (url != null) {
            webView.loadUrl(url)
        } else {
            //TOAST
        }


        // Override shouldOverrideUrlLoading
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                val intent = Intent(this@WebViewActivity, WebViewActivity::class.java)
                intent.putExtra("URL", url)
                startActivity(intent)
                return true
            }
        }

    }
}