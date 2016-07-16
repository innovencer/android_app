package com.advante.golazzos.Helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.Interface.NPayListener;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.npay.hub_cancel_subscription.CancelResponse;
import io.npay.hub_cancel_subscription.OnSubscriptionCancelledListener;
import io.npay.hub_subscriptions.OnSubscriptionCreatedListener;
import io.npay.hub_subscriptions.SubscriptionResponse;

/**
 * Created by Ruben Flores on 7/15/2016.
 */
public class NPay {
    private static NPay singleton;
    private io.npay.activity.NPay npay;
    private Activity activity;
    private ProgressDialog dialog;
    private General gnr;
    private NPayListener nPayListener;

    public NPay(Activity activity) {
        this.activity = activity;
        new General(activity);

        npay = new io.npay.activity.NPay(activity);
        npay.setOnSubscriptionCreatedListener(listenCreate);
        npay.setOnSubscriptionCancelledListener(listenCancel);

        dialog = new ProgressDialog(activity);
        dialog.setCancelable(true);
        dialog.setTitle("");
        dialog.setMessage("Loading...");
    }
    public static synchronized NPay getInstance(Activity activity) {
        if (singleton == null) {
            singleton = new NPay(activity);
        }
        return singleton;
    }

    private OnSubscriptionCreatedListener listenCreate = new OnSubscriptionCreatedListener() {
        @Override
        public void onSubscriptionCreatedListener(SubscriptionResponse result) {
            dialog.dismiss();
            try {
                Log.e("---CREATE---", "*--------------------*");
                Log.v("Object", result.getObject());
                Log.v("Created", result.getCreated());
                Log.v("First Charge", result.getFirstCharge());
                Log.v("Last Charge", result.getLastCharge());
                Log.v("Next Charge", result.getNextCharge());
                Log.v("Customer ID", result.getIdCustomer());
                Log.v("Service ID", result.getIDService());
                Log.v("Cancelled", result.getCancelled());
                Log.v("Status", result.getStatus());
                Log.v("Subscription", result.getIdSubscription());
                createSubscrip(result.getIdSubscription());

                //Toast toast = Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG);
            } catch(Exception e){
                e.printStackTrace();
                dialog.dismiss();
                Toast toast = Toast.makeText(activity, "Toast del lado de la app - Object is empty", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onCancel() {
            dialog.dismiss();
            nPayListener.OnComplete(false);
        }
    };

    private OnSubscriptionCancelledListener listenCancel = new OnSubscriptionCancelledListener() {
        @Override
        public void onSubscriptionCancelledListener(CancelResponse resultItem) {
            try {
                dialog.dismiss();
                Log.e("---CANCEL---", "*--------------------*");
                Log.v("Object", resultItem.getObject());
                Log.v("Created", resultItem.getCreated());
                Log.v("First Charge", resultItem.getFirstCharge());
                Log.v("Last Charge", resultItem.getLastCharge());
                Log.v("Next Charge", resultItem.getNextCharge());
                Log.v("Customer ID", resultItem.getIdCustomer());
                Log.v("Service ID", resultItem.getIDService());
                Log.v("Cancelled", resultItem.getCancelled());
                Log.v("Status", resultItem.getStatus());
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    private void createSubscrip(final String id){
        //dialog.show();
        JSONObject subscription =  new JSONObject();
        JSONObject id_sub = new JSONObject();
        try {
            id_sub.put("id", id);
            subscription.put("subscription", id_sub);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        API.getInstance(activity).authenticateObjectRequest(Request.Method.POST, General.endpoint_subscription, subscription, new API_Listener() {
            @Override
            public void OnSuccess(JSONObject response) {
                gnr.getUser(new IGetUser_Listener() {
                    @Override
                    public void onComplete(Boolean complete, User user) {
                        dialog.dismiss();
                    }
                });
                nPayListener.OnComplete(true);
            }

            @Override
            public void OnSuccess(JSONArray response) {

            }

            @Override
            public void OnError(VolleyError error) {
                dialog.dismiss();
                nPayListener.OnComplete(false);
            }
        });
    }

    public void CreateSubscription(NPayListener nPayListener){
        dialog.show();
        this.nPayListener = nPayListener;
        npay.CreateSubscription(getIdService(), General.KEYWORD, General.MEDIA);
    }

    public void CancelSubscription(){
        dialog.show();
        npay.CancelSubscription().CancelSubscription(gnr.getLoggedUser().getSubscription_id(), getIdService());
    }

    private String getIdService(){
        switch (gnr.locale){
            case "COL":
                return activity.getString(R.string.npay_co);
            case "MEX":
                return activity.getString(R.string.npay_mx);
            default:
                return activity.getString(R.string.npay_co);
        }
    }
}
