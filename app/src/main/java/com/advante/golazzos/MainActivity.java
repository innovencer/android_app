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
import com.advante.golazzos.Model.Counters;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.Model.UserLevel;
import com.advante.golazzos.Model.UserSettings;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

        //preferences.edit().putString("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MywiZXhwaXJlc19hdCI6IjIwMTYtMDYtMDYgMjI6MTg6NTAgVVRDIn0.9-DWmrp6eTWXTYmVRg2F237ShHIr9iXr7M8a1YoD4UA").apply();
        showLog(preferences.getString("token",""));
        if(!preferences.getString("token","").equals("")){
            General.setToken(preferences.getString("token",""));
            getUser();

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

    private void getUser(){
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_users +"/me",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLog(response.toString());
                        User user1 = new User();
                        try {
                            JSONObject data = response.getJSONObject("response");
                            user1.setEmail(data.getString("email"));
                            user1.setId(data.getInt("id"));
                            user1.setName(data.optString("name"));
                            user1.setPaid_subscription(data.getBoolean("paid_subscription"));
                            user1.setSubscription_id(data.getString("subscription_id"));
                            user1.setPoints(data.getDouble("points"));
                            user1.setProfile_pic_url(data.getString("profile_pic_url"));

                            if(!data.isNull("soul_team")){
                                JSONObject soul_team = data.getJSONObject("soul_team");
                                user1.setSoul_team(new SoulTeam(soul_team.getString("image_path"), soul_team.getString("name"), soul_team.getInt("id")));
                            }

                            JSONObject level = data.getJSONObject("level");
                            user1.setLevel(new UserLevel(level.getInt("hits_count"), level.getString("logo_url"),
                                    level.getString("name"), level.getInt("order"),level.getInt("points")));

                            int marcadorTotal_bets = 0, marcadorWon_bets = 0, ganaPierdeWon_bets = 0, ganaPierdeTotal_bets = 0,
                                    total_bets = 0, won_bets = 0;
                            if(data.getJSONObject("counters").has("Marcador"))
                                marcadorTotal_bets = data.getJSONObject("counters").getJSONObject("Marcador").getInt("total_bets");
                            if(data.getJSONObject("counters").has("Marcador"))
                                marcadorWon_bets = data.getJSONObject("counters").getJSONObject("Marcador").getInt("won_bets");
                            if(data.getJSONObject("counters").has("Gana/Pierde"))
                                ganaPierdeTotal_bets = data.getJSONObject("counters").getJSONObject("Gana/Pierde").getInt("total_bets");
                            if(data.getJSONObject("counters").has("Gana/Pierde"))
                                ganaPierdeWon_bets = data.getJSONObject("counters").getJSONObject("Gana/Pierde").getInt("won_bets");
                            if(data.getJSONObject("counters").has("Total"))
                                total_bets = data.getJSONObject("counters").getJSONObject("Total").getInt("total_bets");
                            if(data.getJSONObject("counters").has("Total"))
                                won_bets = data.getJSONObject("counters").getJSONObject("Total").getInt("won_bets");
                            user1.setCounters(new Counters(
                                    marcadorTotal_bets,
                                    marcadorWon_bets,
                                    ganaPierdeTotal_bets,
                                    ganaPierdeWon_bets,
                                    total_bets,
                                    won_bets
                            ));
                            boolean friendship_notification = false;
                            if(!data.getJSONObject("settings").has("friendship_notification")){
                                friendship_notification = true;
                            }else{
                                friendship_notification = data.getJSONObject("settings").getBoolean("friendship_notification");
                            }
                            user1.setUserSettings(new UserSettings(
                                    data.getJSONObject("settings").getBoolean("won_notification"),
                                    data.getJSONObject("settings").getBoolean("lose_notification"),
                                    data.getJSONObject("settings").getBoolean("new_bet_notification"),
                                    friendship_notification,
                                    data.getJSONObject("settings").getBoolean("closed_match_notification")));
                            gnr.setLoggedUser(user1);

                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                            try{
                                File myFile = new File(gnr.local_dir+"tests.txt");
                                myFile.createNewFile();
                                FileOutputStream fOut = new FileOutputStream(myFile);
                                OutputStreamWriter myOutWriter =
                                        new OutputStreamWriter(fOut);
                                myOutWriter.append(General.getToken());
                                myOutWriter.close();
                                fOut.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Error al conectar al servicio",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        /*
                        String body = "";
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        */
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Error al conectar al servicio",Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token "+ General.getToken());
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
