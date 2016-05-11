package com.advante.golazzos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.GeneralActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Ruben Flores on 3/31/2016.
 */
public class MainActivity extends GeneralActivity {
    LinearLayout linearButton1, linearButton2, linearButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearButton1 = (LinearLayout) findViewById(R.id.linearButton1);
        linearButton2 = (LinearLayout) findViewById(R.id.linearButton2);
        linearButton3 = (LinearLayout) findViewById(R.id.linearButton3);

        linearButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
                startActivity(intent);
            }
        });
        linearButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        iniTwitter();
    }

    private void iniTwitter(){
        TwitterAuthConfig authConfig =  new TwitterAuthConfig("aysi160BEoVT7NPkbb5ZLtv8M", "EDYDQIADENYbYkI57nCjsj7xMQu2rJx90x5Y4eaJvPVhn2xGBh");
        Fabric.with(this, new Twitter(authConfig));
    }
}
