package com.advante.golazzos.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 7/1/2016.
 */
public class API {
    private static API singleton;
    private static Context context;
    private JsonObjectRequest objectRequest;
    private JsonArrayRequest arrayRequest;

    private API(Context context) {
        this.context = context;
    }

    public static synchronized API getInstance(Context context) {
        if (singleton == null) {
            singleton = new API(context);
        }
        return singleton;
    }

    private String getToken(){
        SharedPreferences preferences = context.getSharedPreferences(
                General.packetname, Context.MODE_PRIVATE);
        return preferences.getString("token","");
    }

    public void authenticateObjectRequest(int method, final String url, JSONObject request, final API_Listener listener) {
        if (!getToken().equals("")) {
            objectRequest = new JsonObjectRequest(
                    method,
                    url,
                    request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.OnSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.OnError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token " + getToken());
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    7000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(objectRequest);
        }
    }

    public void unAuthenticateObjectRequest(int method, final String url, JSONObject request, final API_Listener listener){
//        dialog.show();
        objectRequest = new JsonObjectRequest(
                method,
                url,
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.OnSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError();
                        listener.OnError(error);
                    }
                });
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }

    public void authenticateArrayRequest(int method, final String url, JSONObject request, final API_Listener listener){
        if (!getToken().equals("")) {
            arrayRequest = new JsonArrayRequest(
                    method,
                    url,
                    request,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            listener.OnSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showError();
                            listener.OnError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token " + getToken());
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    7000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(arrayRequest);
        }
    }

    public void unAuthenticateArrayRequest(int method, final String url, JSONObject request, final API_Listener listener){
        arrayRequest = new JsonArrayRequest(
                method,
                url,
                request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.OnSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError();
                        listener.OnError(error);
                    }
                });
        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(arrayRequest);
    }

    private void showError(){
        Toast.makeText(context, context.getString(R.string.errorComunicaion),Toast.LENGTH_SHORT).show();
    }
    private void showError(VolleyError error){
        Toast.makeText(context, context.getString(R.string.errorComunicaion),Toast.LENGTH_SHORT).show();
        Log.d("","token "+ getToken() +" error "+Log.getStackTraceString(error));
    }
}
