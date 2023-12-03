package com.example.moviesapp.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ActivityWatchFullBinding

class watch_full_activity : AppCompatActivity() {
    lateinit var binding:ActivityWatchFullBinding


    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchFullBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyMovie = intent.getStringExtra("MovieKey")
        webView = binding.videoTrailer
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Cho phép scale để nằm ngang
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)

        // Chuyển WebView về chế độ nằm ngang
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val html = """
    <html>
        <head>
            <style>
                body { margin: 0; padding: 0; }
                iframe { border: none; }
            </style>
        </head>
        <body>
            <iframe width="100%" height="100%" src="https://www.youtube.com/embed/$keyMovie" frameborder="0" allowfullscreen></iframe>
        </body>
    </html>
"""
        binding.videoTrailer.loadData(html, "text/html", "utf-8")
    }
}