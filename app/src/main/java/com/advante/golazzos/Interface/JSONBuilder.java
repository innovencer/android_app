package com.advante.golazzos.Interface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ruben Flores on 7/1/2016.
 */
public class JSONBuilder {

    public static JSONObject UpdateWizard(){
        JSONObject data = new JSONObject();
        JSONObject wizard_status = new JSONObject();
        try {
            wizard_status.put("wizard_status","complete");
            data.put("user",wizard_status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
