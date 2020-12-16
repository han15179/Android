package com.example.final_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class web : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val web: WebView = WebView(this)
        setContentView(web)

        web.webChromeClient = WebChromeClient()
        web.webViewClient = WebViewClient()
        web.settings.javaScriptEnabled = true
        web.settings.defaultTextEncodingName = "utf-8"

        val url = intent.getStringExtra("extra_data")
        if (url!=null){
            web.loadUrl(url)
        }

        val webClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
        web.webViewClient=webClient
    }
}