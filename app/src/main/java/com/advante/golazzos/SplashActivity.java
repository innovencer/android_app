package com.advante.golazzos;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.advante.golazzos.Helpers.Decompress;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Interface.IChangelistener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ruben Flores on 5/2/2016.
 */
public class SplashActivity extends GeneralActivity {
    TextView statusLabel;
    int currentVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        statusLabel = (TextView) findViewById(R.id.statusLabel);
        if(checkreinstall()) {
            retriveUnzip retriveUnzip = new retriveUnzip();
            retriveUnzip.execute();
        }else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class retriveUnzip extends AsyncTask<String,String,Boolean> {
        private Exception exception;
        int cont=1;

        protected Boolean doInBackground(String... urls) {
            String unzipLocation = General.local_dir;
            File filesPath= new File(unzipLocation);

            AssetManager assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = assetManager.openFd("golazzos.zip");
                FileInputStream stream = fileDescriptor.createInputStream();
                Decompress d = new Decompress(stream, unzipLocation, new IChangelistener() {
                    @Override
                    public void onChange(String name) {
                        publishProgress(" Descomprimiendo: "+ cont +" / "+getResources().getString(R.string.dataSize));
                        cont++;
                    }
                });
                d.unzip();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusLabel.setVisibility(View.VISIBLE);
                    statusLabel.setText("Descomprimiendo ...");
                }
            });
        }

        protected void onProgressUpdate(final String... progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusLabel.setVisibility(View.VISIBLE);
                    statusLabel.setText(progress[0]);
                    if(preferences.getInt("installedVersion",0) != currentVersion) {
                        preferences.edit().putInt("installedVersion", currentVersion).apply();
                    }

                }
            });
        }

        protected void onPostExecute(Boolean result) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean checkreinstall(){
        PackageInfo pInfo = null;
        boolean ret = false;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionCode;
            if(preferences.getInt("installedVersion",0) < currentVersion){
                ret = true;
            }
            String unzipLocation = General.local_dir;
            File filesPath= new File(unzipLocation);
            if(!filesPath.exists()) {
                filesPath.mkdirs();
                ret = true;
            }else{
                getFilesCount getFilesCount =  new getFilesCount();
                getFilesCount.getFile(unzipLocation);
                showLog(unzipLocation+" "+getFilesCount.count);
                if(getFilesCount.count < Integer.parseInt(getResources().getString(R.string.dataSize))){
                    ret = true;
                }
            }
            return ret;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    class getFilesCount{
        int count = 0;

        public getFilesCount() {
        }

        private void getFile(String dirPath) {
            File f = new File(dirPath);
            File[] files = f.listFiles();

            if (files != null)
                for (int i = 0; i < files.length; i++) {
                    count++;
                    File file = files[i];

                    if (file.isDirectory()) {
                        getFile(file.getAbsolutePath());
                    }
                }
        }
    }
}
