package com.advante.golazzos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.NPay;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.Interface.NPayListener;
import com.advante.golazzos.Model.User;

import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
                            final Dialog dialog1 = new Dialog(EstadisticasActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.dialog_titular);
                            ImageView image = (ImageView) dialog1.findViewById(R.id.image);

                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                    wb = (WebView) findViewById(R.id.webView);
                                    disableCertificateValidation();
                                    wb.setWebViewClient(new webViewClient());
                                    webSettings = wb.getSettings();
                                    webSettings.setJavaScriptEnabled(true);

                                    wb.loadUrl(getIntent().getStringExtra("html_center_url"));
                                    linear1.setVisibility(View.VISIBLE);
                                    linear2.setVisibility(View.GONE);
                                }
                            });
                            dialog1.show();
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
            showLog(error.toString());
            builder.setMessage(getString(R.string.dialog_Estadisticas_Message));
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                    finish();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();

            /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(Tab1Activity.this);
        AlertDialog alertDialog = builder.create();
        String message = "SSL Certificate error.";
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = "The certificate authority is not trusted.";
                break;
            case SslError.SSL_EXPIRED:
                message = "The certificate has expired.";
                break;
            case SslError.SSL_IDMISMATCH:
                message = "The certificate Hostname mismatch.";
                break;
            case SslError.SSL_NOTYETVALID:
                message = "The certificate is not yet valid.";
                break;
        }

        message += " Do you want to continue anyway?";
        alertDialog.setTitle("SSL Certificate Error");
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ignore SSL certificate errors
                handler.proceed();
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                handler.cancel();
            }
        });
        alertDialog.show();
            */
        }
    }

    public static void disableCertificateValidation() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }};

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {}
    }
}
