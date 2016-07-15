package com.advante.golazzos.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.advante.golazzos.Interface.API_Listener;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/26/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    JsonObjectRequest jsArrayRequest;
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
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

        API.getInstance(this).authenticateObjectRequest(Request.Method.PUT, General.endpoint_users + "/me", user, new API_Listener() {
            @Override
            public void OnSuccess(JSONObject response) {
                SharedPreferences preferences = MyFirebaseInstanceIDService.this.getSharedPreferences(
                        General.packetname, Context.MODE_PRIVATE);
                preferences.edit().putString("device_registration_id",device_registration_id).apply();
            }

            @Override
            public void OnSuccess(JSONArray response) {

            }

            @Override
            public void OnError(VolleyError error) {

            }
        });

    }

}