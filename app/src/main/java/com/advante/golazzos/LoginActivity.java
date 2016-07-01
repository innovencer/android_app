package com.advante.golazzos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.Map;

/**
 * Created by Ruben Flores on 4/10/2016.
 */
public class LoginActivity extends GeneralActivity {
    private LoginButton loginButton;
    private EditText userEditText;
    private EditText passwordEditText;
    LinearLayout linearButton1;
    RelativeLayout backButton;
    private CallbackManager callbackManager;
    JsonObjectRequest jsArrayRequest;
    boolean stayFBConnected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        initFacebookButton();
    }

    private void initUI(){

        userEditText = (EditText) findViewById(R.id.textEmail);
        passwordEditText = (EditText) findViewById(R.id.textPassword);
        backButton = (RelativeLayout) findViewById(R.id.backButton);

        if(isFacebookLoggedIn()){
            if(stayFBConnected)
                loginFB(AccessToken.getCurrentAccessToken().getToken());
            else{
                LoginManager.getInstance().logOut();
            }
        }

        linearButton1 = (LinearLayout) findViewById(R.id.linearButton1);
        linearButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                loginCredential(userEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
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
                        LoginActivity.this,
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
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
                            General.setToken(data.getString("jwt"));
                            gnr.getUser(iGetUser_listener);
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
                        try {
                            JSONObject data = response.getJSONObject("response");
                            General.setToken(data.getString("jwt"));
                            gnr.getUser(iGetUser_listener);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores

                        String body = "";
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if(statusCode != null && statusCode.equals("422")){
                            if(error.networkResponse.data!=null) {
                                try {
                                    body = new String(error.networkResponse.data,"UTF-8");
                                    showShortToast("Correo o Clave incorrectas");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            showShortToast("Error en la comunicacion, por favor intente mas tarde.");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    IGetUser_Listener iGetUser_listener = new IGetUser_Listener() {
        @Override
        public void onComplete(Boolean complete) {
            if(complete) {
                if (gnr.getLoggedUser().getWizzard().isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoginActivity.this, Wizzard1Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

 public void recordarPass(View v){
     final Dialog dialog1 = new Dialog(this,android.R.style.Theme_DeviceDefault_Dialog);
     dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
     dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
     dialog1.setContentView(R.layout.dialog_password_1);
     final EditText email = (EditText) dialog1.findViewById(R.id.editEmail);
     LinearLayout btnAceptar = (LinearLayout) dialog1.findViewById(R.id.btnAceptar);
     LinearLayout btnCancelar = (LinearLayout) dialog1.findViewById(R.id.btnCancelar);

     btnCancelar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             dialog1.dismiss();
         }
     });
     btnAceptar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(email.getText().toString().length()>0){
                 dialog.show();
                 JSONObject password = new JSONObject();
                 JSONObject email_json = new JSONObject();

                 try {
                     email_json.put("email", email.getText().toString());
                     password.put("password", email_json);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 showLog(password.toString());
                 jsArrayRequest = new JsonObjectRequest(
                         Request.Method.POST,
                         General.endpoint_password,
                         password,
                         new Response.Listener<JSONObject>() {
                             @Override
                             public void onResponse(JSONObject response) {
                                 dialog1.dismiss();
                                 dialog.dismiss();
                                 setPassowrd();
                             }
                         },
                         new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 // Manejo de errores
                                 dialog1.dismiss();
                                 dialog.dismiss();
                                 Toast.makeText(LoginActivity.this,"Error al conectar al servicio",Toast.LENGTH_SHORT).show();
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
                 VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsArrayRequest);
             }
         }
     });
     dialog1.show();
 }

    private void setPassowrd(){
        final Dialog dialog1 = new Dialog(this,android.R.style.Theme_DeviceDefault_Dialog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_password_2);
        final EditText password1 = (EditText) dialog1.findViewById(R.id.editPassword);
        final EditText password2 = (EditText) dialog1.findViewById(R.id.editPassword2);
        final EditText token = (EditText) dialog1.findViewById(R.id.editToken);
        LinearLayout btnAceptar = (LinearLayout) dialog1.findViewById(R.id.btnAceptar);
        LinearLayout btnCancelar = (LinearLayout) dialog1.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password1.getText().toString().length()>0){
                    dialog.show();
                    JSONObject password = new JSONObject();
                    JSONObject data = new JSONObject();

                    try {
                        data.put("reset_password_token", token.getText().toString());
                        data.put("password", password1.getText().toString());
                        data.put("password_confirmation", password2.getText().toString());
                        password.put("password", data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showLog(password.toString());
                    jsArrayRequest = new JsonObjectRequest(
                            Request.Method.PUT,
                            General.endpoint_password,
                            password,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    dialog.dismiss();
                                    showShortToast("Su clave se ha cambiado exitosamente.");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"Token incorrecto.",Toast.LENGTH_SHORT).show();
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
                    VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsArrayRequest);
                }
            }
        });
        dialog1.show();
    }
}
