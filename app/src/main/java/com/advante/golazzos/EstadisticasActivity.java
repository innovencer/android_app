package com.advante.golazzos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.NPay;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.Interface.NPayListener;
import com.advante.golazzos.Model.User;

/**
 * Created by Ruben Flores on 4/23/2016.
 */
public class EstadisticasActivity extends GeneralActivity {
    WebView wb;
    LinearLayout buttonBack, linear1, linear2;
    private WebSettings webSettings;
    private NPay npay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        npay = new NPay(this);

        buttonBack = (LinearLayout) findViewById(R.id.buttonBack);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);

        gnr.getUser(new IGetUser_Listener() {
            @Override
            public void onComplete(Boolean complete, User user) {
                if(complete){
                    if(user.getPaid_subscription()){
                        wb=(WebView)findViewById(R.id.webView);
                        wb.setWebViewClient(new webViewClient());
                        webSettings = wb.getSettings();
                        webSettings.setJavaScriptEnabled(true);

                        wb.loadUrl(getIntent().getStringExtra("html_center_url"));
                        linear1.setVisibility(View.VISIBLE);
                        linear2.setVisibility(View.GONE);
                    }else {
                        linear1.setVisibility(View.GONE);
                        linear2.setVisibility(View.VISIBLE);
                    }
                }else {
                    linear1.setVisibility(View.GONE);
                    linear2.setVisibility(View.VISIBLE);
                }
            }
        });

        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CreateSubscription(new NPayListener() {
                    @Override
                    public void OnComplete(Boolean pass) {
                        if(pass) {
                            wb = (WebView) findViewById(R.id.webView);
                            wb.setWebViewClient(new webViewClient());
                            webSettings = wb.getSettings();
                            webSettings.setJavaScriptEnabled(true);

                            wb.loadUrl(getIntent().getStringExtra("html_center_url"));
                            linear1.setVisibility(View.VISIBLE);
                            linear2.setVisibility(View.GONE);
                        }else{
                            linear1.setVisibility(View.GONE);
                            linear2.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

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
        public void onReceivedSslError(WebView view,final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(EstadisticasActivity.this);
            builder.setMessage("Server SSL Certificate seems invalid. Do you want to continue ?");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
