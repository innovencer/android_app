package com.advante.golazzos.Helpers;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.OkHttpDownloader;

/**
 * Created by Ruben Flores on 7/15/2016.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        com.squareup.picasso.Picasso.Builder builder = new com.squareup.picasso.Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        com.squareup.picasso.Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        com.squareup.picasso.Picasso.setSingletonInstance(built);

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }

    public static Context getContext(){
        return mContext;
    }
}