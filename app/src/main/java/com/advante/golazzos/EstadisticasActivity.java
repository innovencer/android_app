package com.advante.golazzos;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.advante.golazzos.Helpers.GeneralActivity;

/**
 * Created by Ruben Flores on 4/23/2016.
 */
public class EstadisticasActivity extends GeneralActivity {
    WebView wb;
    LinearLayout buttonBack;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        buttonBack = (LinearLayout) findViewById(R.id.buttonBack);
        wb=(WebView)findViewById(R.id.webView);
        wb.setWebViewClient(new webViewClient());
        webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wb.loadUrl(getIntent().getStringExtra("html_center_url"));

        showLog(getIntent().getStringExtra("html_center_url"));
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }
}
