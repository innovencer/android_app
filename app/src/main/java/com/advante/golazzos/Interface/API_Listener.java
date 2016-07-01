package com.advante.golazzos.Interface;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Ruben Flores on 7/1/2016.
 */
public interface API_Listener {
    void OnSuccess(JSONObject response);
    void OnSuccess(JSONArray response);
    void OnError(VolleyError error);
}
