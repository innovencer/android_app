package com.advante.golazzos.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        HashMap<String, String> values = new HashMap<String, String>();
        try {
            if (intent.hasExtra("referrer")) {
                Log.d("Golazzos", "referrer: " + URLDecoder.decode(intent.getStringExtra("referrer")));
                String referrers[] = URLDecoder.decode(intent.getStringExtra("referrer")).split("&");
                for (String referrerValue : referrers) {
                    String keyValue[] = referrerValue.split("=");
                    values.put(URLDecoder.decode(keyValue[0]), URLDecoder.decode(keyValue[1]));
                }
                Log.d("Golazzos", "referrer: " + values);
                try{
                    File myFile = new File(General.local_dir+"referrer.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append("referrer "+values);
                    myOutWriter.close();
                    fOut.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }

    }
}
