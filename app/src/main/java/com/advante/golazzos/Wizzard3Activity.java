package com.advante.golazzos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.API;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.Helpers.JSONBuilder;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 4/12/2016.
 */
public class Wizzard3Activity extends GeneralActivity {
    JsonObjectRequest jsArrayRequest;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    LinearLayout buttonFB, buttonTW, buttonMA;
    TextView buttonSiguiente;
    ImageView imageFB, imageTW, imageMA;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizzard3);

        callbackManager = CallbackManager.Factory.create();

        buttonFB = (LinearLayout) findViewById(R.id.buttonFB);
        buttonTW = (LinearLayout) findViewById(R.id.buttonTW);
        buttonMA = (LinearLayout) findViewById(R.id.buttonMA);
        buttonSiguiente = (TextView) findViewById(R.id.buttonSiguiente);

        imageFB = (ImageView) findViewById(R.id.imageFB);
        imageTW = (ImageView) findViewById(R.id.imageTW);
        imageMA = (ImageView) findViewById(R.id.imageMA);

        //changeColorButton(1);

        buttonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnFacebook();
            }
        });
        buttonTW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishTwitterFeed();
            }
        });
        buttonMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","", null));
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Te invito a Jugar a Golazzos");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Te gustan los RETOS? ATREVETE a competir conmigo en GOLAZZOS, el primer JUEGO SOCIAL de predicciones de FUTBOL! Decarga la APP (https://play.google.com/store/apps/details?id=com.advante.golazzos&referrer=utm_source%3Dfacebook%26utm_medium%3Dmessenger%26utm_content%3Did%253A"+gnr.getLoggedUser().getInvitation_token()+")");
                startActivity(Intent.createChooser(emailIntent, "Golazzos"));
            }
        });
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                API.getInstance(Wizzard3Activity.this).authenticateObjectRequest(Request.Method.PUT, gnr.endpoint_users + "/me", JSONBuilder.UpdateWizard(), new API_Listener() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        Intent intent = new Intent(Wizzard3Activity.this,Wizzard4Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void OnSuccess(JSONArray response) {

                    }

                    @Override
                    public void OnError(VolleyError error) {

                    }
                });

            }
        });
    }

    private void publishTwitterFeed(){
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("El primer JUEGO SOCIAL de predicciones de FUTBOL! Decarga la APP (https://play.google.com/store/apps/details?id=com.advante.golazzos&referrer=utm_source%3Dfacebook%26utm_medium%3Dmessenger%26utm_content%3Did%253A"+gnr.getLoggedUser().getInvitation_token()+")");
        builder.show();
    }

    private void shareOnFacebook(){
        shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setContentDescription(
                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            shareDialog.show(linkContent);
        }
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                addPoints(300);
                changeColorButton(1);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void addPoints(int points){
        points = points * 100;
        dialog.show();
        JSONObject post = new JSONObject();
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("amount_centavos", points);
            post.put("point_charge",parametros);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_point_charges,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        dialog.dismiss();
                        try {
                            JSONObject data = response.getJSONObject("response");
                            showLog(data.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showShortToast("Error en la comunicacion, por favor intente mas tarde.");
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token "+ gnr.getToken());
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void changeColorButton(int button){
        LinearLayout layout = null;
        switch (button){
            case 1:
                layout = buttonFB;
                imageFB.setImageResource(R.drawable.facebook_logo_blue);
                break;
            case 2:
                layout = buttonTW;
                break;
            case 3:
                layout = buttonMA;
                break;
        }
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.background_green) );
        } else {
            layout.setBackground( getResources().getDrawable(R.drawable.background_green));
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

    }
}
