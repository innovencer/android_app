package com.advante.golazzos.Helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;


/**
 * Created by Ruben Flores on 1/18/2016.
 */
public class GeneralActivity extends Activity {
    private String TAG = General.appname;
    public SharedPreferences preferences;
    public General gnr;
    public ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        preferences = this.getSharedPreferences(
                General.packetname, Context.MODE_PRIVATE);
        gnr = new General(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("");
        dialog.setCancelable(false);
        dialog.setMessage("Conectando...");
        initgetSocial();
    }

    public void showLog(String text){
        Log.d(TAG, text);
    }

    public void showLog(int text){
        Log.d(TAG, ""+ text);
    }

    public void showShortToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void initgetSocial() {

    }
}
