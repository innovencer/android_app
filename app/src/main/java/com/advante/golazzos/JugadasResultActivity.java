package com.advante.golazzos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.GraphicsUtil;

import java.io.File;

/**
 * Created by Ruben Flores on 6/9/2016.
 */
public class JugadasResultActivity extends GeneralActivity {
    TextView textLabel, textTime_ago, textTitulo, textPuntosJuegas, textPuntosGanas;
    ImageView imageEquipo1;
    LinearLayout buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras.getBoolean("result")) {
            setContentView(R.layout.activity_jugadas_finalizado_1);
        }else{
            setContentView(R.layout.activity_jugadas_finalizado_2);
        }

        textLabel = (TextView) findViewById(R.id.textLabel);
        textTime_ago = (TextView) findViewById(R.id.textTime_ago);
        textTitulo = (TextView) findViewById(R.id.textTitulo);
        textPuntosJuegas = (TextView) findViewById(R.id.textPuntosJuegas);
        textPuntosGanas = (TextView) findViewById(R.id.textPuntosGanas);

        imageEquipo1 = (ImageView) findViewById(R.id.imageEquipo1);

        buttonBack = (LinearLayout) findViewById(R.id.buttonBack);

        textLabel.setText(extras.getString("textLabel"));
        textTime_ago.setText(extras.getString("textTime_ago"));
        textTitulo.setText(extras.getString("option"));
        textPuntosJuegas.setText(""+extras.getInt("amount"));
        textPuntosGanas.setText(""+extras.getInt("amount_to_deposit"));

        File file;
        Bitmap bm;
        GraphicsUtil graphicUtil = new GraphicsUtil();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if(extras.getInt("image") > 0){
            file = new File(General.local_dir_images + "equipos/"+ extras.getInt("image") +".gif");
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }else{
            imageEquipo1.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main));
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}
