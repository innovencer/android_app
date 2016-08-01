package com.advante.golazzos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.API;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.JSONBuilder;
import com.advante.golazzos.Helpers.NPay;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.Interface.NPayListener;
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
public class Wizzard4Activity extends GeneralActivity {
    TextView buttonSiguiente;
    ImageView image1;
    NPay npay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizzard4);

        npay = new NPay(this);

        buttonSiguiente = (TextView) findViewById(R.id.buttonSiguiente);
        image1 = (ImageView) findViewById(R.id.image1);

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                API.getInstance(Wizzard4Activity.this).authenticateObjectRequest(Request.Method.PUT, gnr.endpoint_users + "/me", JSONBuilder.UpdateWizard(), new API_Listener() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        Intent intent = new Intent(Wizzard4Activity.this,PrincipalActivity.class);
                        intent.putExtra("wizzard","true");
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
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CreateSubscription(new NPayListener() {
                    @Override
                    public void OnComplete(Boolean pass) {
                        Intent intent = new Intent(Wizzard4Activity.this,PrincipalActivity.class);
                        intent.putExtra("wizzard","true");
                        intent.putExtra("premium","true");
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
