package com.advante.golazzos.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.advante.golazzos.Interface.API_Listener;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 7/1/2016.
 */
public class API {
    private static API singleton;
    private static String token;
    private static Context context;
    JsonObjectRequest objectRequest;
    JsonArrayRequest arrayRequest;

    private API(Context context) {
        API.context = context;
        SharedPreferences preferences = context.getSharedPreferences(General.packetname, Context.MODE_PRIVATE);
        API.token = preferences.getString("token","");
    }

    public static synchronized API getInstance(Context context) {
        if (singleton == null) {
            singleton = new API(context);
        }
        return singleton;
    }

    public void authenticateObjectRequest(int method, final String url, JSONObject request, final API_Listener listener){
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
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

    public void authenticateArrayRequest(int method, final String url, JSONObject request, final API_Listener listener){
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
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
