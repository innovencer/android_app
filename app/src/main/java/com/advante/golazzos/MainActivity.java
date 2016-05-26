package com.advante.golazzos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Counters;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.Model.UserLevel;
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

        if(!preferences.getString("token","").equals("")){
            General.setToken(preferences.getString("token",""));
            getUser();
        }else{
            linearButton1.setVisibility(View.VISIBLE);
            linearButton2.setVisibility(View.VISIBLE);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        showLog("Subscribed to news topic");
        showShortToast("InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        iniTwitter();
    }

    private boolean isRegistered() {
        //Getting shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(General.SHARED_PREF, MODE_PRIVATE);

        //Getting the value from shared preferences
        //The second parameter is the default value
        //if there is no value in sharedprference then it will return false
        //that means the device is not registered
        return sharedPreferences.getBoolean(General.REGISTERED, false);
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
                            user1.setPoints(data.getDouble("points"));
                            user1.setProfile_pic_url(data.getString("profile_pic_url"));

                            if(!data.isNull("soul_team")){
                                JSONObject soul_team = data.getJSONObject("soul_team");
                                user1.setSoul_team(new SoulTeam(soul_team.getString("image_path"), soul_team.getString("name"), soul_team.getInt("id")));
                            }

                            JSONObject level = data.getJSONObject("level");
                            user1.setLevel(new UserLevel(level.getInt("hits_count"), level.getString("logo_url"),
                                    level.getString("name"), level.getInt("order"),level.getInt("points")));

                            user1.setCounters(new Counters(
                                    data.getJSONObject("counters").getJSONObject("Marcador").getInt("total_bets"),
                                    data.getJSONObject("counters").getJSONObject("Marcador").getInt("won_bets"),
                                    data.getJSONObject("counters").getJSONObject("Gana/Pierde").getInt("total_bets"),
                                    data.getJSONObject("counters").getJSONObject("Gana/Pierde").getInt("won_bets"),
                                    data.getJSONObject("counters").getJSONObject("Total").getInt("total_bets"),
                                    data.getJSONObject("counters").getJSONObject("Total").getInt("won_bets")
                            ));

                            gnr.setLoggedUser(user1);

                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
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
}
