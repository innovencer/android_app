package com.advante.golazzos;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.facebook.share.widget.SendButton;
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
    LinearLayout buttonFB, buttonTW, buttonMA, buttonOT;
    TextView buttonSiguiente;
    ImageView imageFB, imageTW, imageMA;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizzard3);

        callbackManager = CallbackManager.Factory.create();

        buttonFB = (LinearLayout) findViewById(R.id.buttonFB);
        buttonTW = (LinearLayout) findViewById(R.id.buttonTW);
        buttonMA = (LinearLayout) findViewById(R.id.buttonMA);
        buttonOT = (LinearLayout) findViewById(R.id.buttonOthers);

        buttonSiguiente = (TextView) findViewById(R.id.buttonSiguiente);

        imageFB = (ImageView) findViewById(R.id.imageFB);
        imageTW = (ImageView) findViewById(R.id.imageTW);
        imageMA = (ImageView) findViewById(R.id.imageMA);

        callbackManager = CallbackManager.Factory.create();
        SendButton sendButton = (SendButton) findViewById(R.id.button_facebook);
        sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                showLog(result.toString());
            }

            @Override
            public void onCancel() {
                showLog("cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                showLog(error.getMessage());
            }
        });

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Juega Conmigo Golazzos!")
                .setContentDescription("ATREVETE a competir conmigo en GOLAZZOS, el primer JUEGO SOCIAL de predicciones de FUTBOL! Descarga la APP.")
                .setContentUrl(Uri.parse(getString(R.string.baseLanding)+gnr.getLoggedUser().getInvitation_token()))
                .setImageUrl(Uri.parse(getString(R.string.baseLandingIco)))
                .build();
        sendButton.setShareContent(linkContent);

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
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_SUBJECT, "Golazzos");
                i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.share_Email)+"&referrer="+gnr.getLoggedUser().getInvitation_token()+")");
                try {
                    startActivity(Intent.createChooser(i, "Send Mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Wizzard3Activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String link = getString(R.string.share_Email)+"&referrer="+gnr.getLoggedUser().getInvitation_token()+")";
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(link);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Se ha copiado el link", link);
                    clipboard.setPrimaryClip(clip);
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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
                .text(getString(R.string.share_Twitter)+"&referrer="+gnr.getLoggedUser().getInvitation_token()+")");
        builder.show();
    }

    private void shareOnFacebook(){

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
