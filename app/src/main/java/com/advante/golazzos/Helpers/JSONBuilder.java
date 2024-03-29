package com.advante.golazzos.Helpers;

import android.graphics.Bitmap;
import android.util.Log;

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

    public static JSONObject addFriend(String invitationToken){
        JSONObject root = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("invitation_token",invitationToken);
            root.put("invitation",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }

    public static JSONObject changeAvatar(Bitmap bitmap){
        JSONObject root = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            String base64 = "data:image/jpeg;base64,"+ General.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            data.put("avatar_base_64",base64);
            root.put("user",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }
}
