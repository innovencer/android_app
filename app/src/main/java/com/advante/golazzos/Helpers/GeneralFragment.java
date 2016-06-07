package com.advante.golazzos.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Ruben Flores on 1/21/2016.
 */
public class GeneralFragment  extends Fragment {
    private String TAG = General.appname;
    public SharedPreferences preferences;
    public General gnr;
    public ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences(
                General.packetname, Context.MODE_PRIVATE);
        gnr = new General(getContext());
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("");
        dialog.setMessage("Conectando...");
    }
    public void showLog(String text){
        Log.d(TAG, text);
    }

    public void showLog(int text){
        Log.d(TAG, ""+ text);
    }

    public void showShortToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
