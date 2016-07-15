package com.advante.golazzos.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.advante.golazzos.Interface.API_Listener;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Ruben Flores on 7/2/2016.
 */
public class CustomInstallTrackersReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        General gnr = new General(context);
        SharedPreferences preferences = context.getSharedPreferences(
                General.packetname, Context.MODE_PRIVATE);
        HashMap<String, String> values = new HashMap<String, String>();
        try {
            if (intent.hasExtra("referrer")) {
                //Log.d("Golazzos", "referrer: " + URLDecoder.decode(intent.getStringExtra("referrer")));
                String referrers[] = URLDecoder.decode(intent.getStringExtra("referrer")).split("&");
                for (String referrerValue : referrers) {
                    String keyValue[] = referrerValue.split("=");
                    values.put(URLDecoder.decode(keyValue[0]), URLDecoder.decode(keyValue[1]));
                }
                //Log.d("Golazzos", "referrer: " + values);
                String invitation = values.get("utm_content").split(":")[1];
                preferences.edit().putString("invitation_token",invitation).apply();
            }
        } catch (Exception e) {

        }

    }
}
