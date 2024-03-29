package com.advante.golazzos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.IGetUser_Listener;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ruben Flores on 3/31/2016.
 */
public class CrearCuentaActivity extends GeneralActivity {
    private LoginButton loginButton;
    private EditText userEditText;
    private EditText passwordEditText;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    LinearLayout linearButton1;
    private CallbackManager callbackManager;
    JsonObjectRequest jsArrayRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        initUI();
        initFacebookButton();
    }

    private void initUI(){

        userEditText = (EditText) findViewById(R.id.textEmail);
        passwordEditText = (EditText) findViewById(R.id.textPassword);
        firstnameEditText = (EditText) findViewById(R.id.textFirstName);
        lastnameEditText = (EditText) findViewById(R.id.textLastName);

        linearButton1 = (LinearLayout) findViewById(R.id.linearButton1);

        linearButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                registrar(General.endpoint_users, userEditText.getText().toString(), passwordEditText.getText().toString(),
                        firstnameEditText.getText().toString(), lastnameEditText.getText().toString());
            }
        });
    }

    private void initFacebookButton(){
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                LoginManager.getInstance().logInWithPublishPermissions(
                        CrearCuentaActivity.this,
                        Arrays.asList("publish_actions"));
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            loginFB(accessToken.getToken());
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    loginFB(accessToken.getToken());
                }
            }

            @Override
            public void onCancel() {
                // Nothing to do here.
            }


            @Override
            public void onError(FacebookException e) {
                Toast.makeText(CrearCuentaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registrar(String url, final String email, final String password, String first_name, String last_name){
        JSONObject parametros = new JSONObject();
        final JSONObject user = new JSONObject();

        try {
            parametros.put("email", email);
            parametros.put("password", password);
            parametros.put("first_name", first_name);
            parametros.put("last_name", last_name);

            user.put("user",parametros);

            showLog(user.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        User user1 = new User();
                        try {
                            JSONObject data = response.getJSONObject("response");
                            user1.setEmail(data.getString("email"));
                            user1.setId(data.getInt("id"));
                            user1.setName(data.optString("name"));
                            user1.setPaid_subscription(data.getBoolean("paid_subscription"));
                            user1.setPoints(data.getDouble("points"));
                            user1.setProfile_pic_url(data.getString("profile_pic_url"));
                            user1.setInvitation_token(data.getString("invitation_token"));

                            JSONObject level = data.getJSONObject("level");

                            user1.setLevel(new UserLevel(level.getInt("hits_count"), level.getString("logo_url"),
                                    level.getString("name"), level.getInt("order"),level.getInt("points")));

                            gnr.setLoggedUser(user1);
                            loginCredential(email, password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                            String msg = jsonObject.getString("messages");
                            String msgFinal = "";
                            for(int i = 0;i < msg.split(",").length;i++){
                                msgFinal = msgFinal + msg.split(",")[i] + System.getProperty("line.separator");
                            }

                            msgFinal = msgFinal.replace("[","").replace("]","").replace("{","").replace("}","");

                            showShortToast(msgFinal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void loginCredential(String email, String password){
        JSONObject post = new JSONObject();
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("from", "credentials");
            parametros.put("email", email);
            parametros.put("password", password);
            post.put("token", parametros);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_tokens,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        dialog.dismiss();
                        try {
                            JSONObject data = response.getJSONObject("response");

                            gnr.setToken(data.getString("jwt"));

                            Intent intent = new Intent(CrearCuentaActivity.this,Wizzard1Activity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        dialog.dismiss();
                        showShortToast("Error en la comunicacion, por favor intente mas tarde.");

                    }
                });
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void loginFB(String token){
        dialog.show();
        JSONObject post = new JSONObject();
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("from", "facebook");
            parametros.put("value", token);
            post.put("token",parametros);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_tokens,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        dialog.dismiss();
                        try {
                            JSONObject data = response.getJSONObject("response");
                            gnr.setToken(data.getString("jwt"));
                            gnr.getUser(new IGetUser_Listener() {
                                @Override
                                public void onComplete(Boolean complete, User user) {
                                    if(complete){
                                        Intent intent = new Intent(CrearCuentaActivity.this,Wizzard1Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        dialog.dismiss();
                        showShortToast("Error en la comunicacion, por favor intente mas tarde.");

                    }
                });
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
