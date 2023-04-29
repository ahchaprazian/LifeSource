/**
 *  WebActivity is an Android activity that displays web pages using a WebView.
 *  It is part of the com.example.lifesoruce package. This activity is responsible
 *  for loading and displaying a web page from a given URL that is passed through
 *  an intent.
 *  The activity utilizes ActivityWebBinding to access the views of its layout
 *  and manage the WebView for loading and displaying the web page.
 */
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

    /**
     * Initializes the ActivityWebBinding, sets the content view, and loads the
     * URL passed through the intent into the WebView.
     *
     * @param savedInstanceState A Bundle containing the most recent data provided
     *                           in onSaveInstanceState(Bundle)
     * @see ActivityWebBinding
     * @see WebView
     */
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