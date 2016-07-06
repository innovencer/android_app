package com.advante.golazzos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.Model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Ruben Flores on 3/31/2016.
 */
public class MainActivity extends GeneralActivity {
    LinearLayout linearButton1, linearButton2, linearButton3;
    JsonObjectRequest jsArrayRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearButton1 = (LinearLayout) findViewById(R.id.linearButton1);
        linearButton2 = (LinearLayout) findViewById(R.id.linearButton2);
        linearButton3 = (LinearLayout) findViewById(R.id.linearButton3);

        linearButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
                startActivity(intent);
            }
        });
        linearButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        preferences.edit().putString("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MywiZXhwaXJlc19hdCI6IjIwMTYtMDYtMDYgMjI6MTg6NTAgVVRDIn0.9-DWmrp6eTWXTYmVRg2F237ShHIr9iXr7M8a1YoD4UA").apply();
        //showLog(preferences.getString("token",""));
        if(!preferences.getString("token","").equals("")){
            General.setToken(preferences.getString("token",""));
            gnr.getUser(new IGetUser_Listener() {
                @Override
                public void onComplete(Boolean complete, User user) {
                    if(complete){
                        if (user.getWizzard().equals("complete")) {
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(MainActivity.this, Wizzard1Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });

        }else{
            linearButton1.setVisibility(View.VISIBLE);
            linearButton2.setVisibility(View.VISIBLE);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        if(FirebaseInstanceId.getInstance().getToken() != null && !FirebaseInstanceId.getInstance().getToken().isEmpty() ){
            if(!FirebaseInstanceId.getInstance().getToken().equals(preferences.getString("device_registration_id",""))) {
                sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
            }
        }
        //showShortToast("InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        iniTwitter();
    }

    private void iniTwitter(){
        TwitterAuthConfig authConfig =  new TwitterAuthConfig("aysi160BEoVT7NPkbb5ZLtv8M", "EDYDQIADENYbYkI57nCjsj7xMQu2rJx90x5Y4eaJvPVhn2xGBh");
        Fabric.with(this, new Twitter(authConfig));
    }


    private void sendRegistrationToServer(final String device_registration_id) {
        JSONObject user = new JSONObject();
        JSONObject values = new JSONObject();
        try {
            values.put("device_registration_id", device_registration_id);

            user.put("user",values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                General.endpoint_users+"/me",
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences preferences = MainActivity.this.getSharedPreferences(
                                General.packetname, Context.MODE_PRIVATE);
                            preferences.edit().putString("device_registration_id",device_registration_id).apply();
                            Toast.makeText(MainActivity.this,"Device ID Actualizado",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error en al tratar de conectar con el servicio web. Intente mas tarde",Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + General.getToken());
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
}
