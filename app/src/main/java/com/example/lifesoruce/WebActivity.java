package com.example.lifesoruce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifesoruce.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {
    // setting the TAG for debugging purposes
    private static String TAG="WebActivity";

    // declaring the webview
    private WebView myWebView;

    // declaring the url string variable
    private String url;
    private ActivityWebBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWebBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        myWebView = binding.webview;
        Intent intent = getIntent();

        if(intent != null) {
            url = intent.getStringExtra("url_key");

            myWebView.loadUrl(url);
        }
    }

    @Override
    protected  void onRestart() {
        super.onRestart();
        finish();
    }
}