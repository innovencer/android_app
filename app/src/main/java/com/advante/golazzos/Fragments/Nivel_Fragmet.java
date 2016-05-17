package com.advante.golazzos.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.R;

import java.io.File;

/**
 * Created by Ruben Flores on 5/8/2016.
 */
public class Nivel_Fragmet extends GeneralFragment {
    TextView textNivel, textTituloNivel, textAciertos, textPuntos, textTituloNivel1;
    TextView textNivel0, textNivel1, textNivel2, textNivel3, textNivel4, textNivel5, textNivel6, textNivel7,
             textNivel8, textNivel9, textNivel10, textNivel11, textNivel12, textNivel13, textNivel14, textNivel15;
    ImageView imageTrofeo;
    BitmapFactory.Options options;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nivel, container, false);

        textAciertos = (TextView) view.findViewById(R.id.textAciertos);
        textNivel = (TextView) view.findViewById(R.id.textNivel);
        textTituloNivel = (TextView) view.findViewById(R.id.textTituloNivel);
        textPuntos = (TextView) view.findViewById(R.id.textPuntos);
        textTituloNivel1 = (TextView) view.findViewById(R.id.textTituloNivel1);

        textNivel0 = (TextView) view.findViewById(R.id.textNivel0);
        textNivel1 = (TextView) view.findViewById(R.id.textNivel1);
        textNivel2 = (TextView) view.findViewById(R.id.textNivel2);
        textNivel3 = (TextView) view.findViewById(R.id.textNivel3);
        textNivel4 = (TextView) view.findViewById(R.id.textNivel4);
        textNivel5 = (TextView) view.findViewById(R.id.textNivel5);
        textNivel6 = (TextView) view.findViewById(R.id.textNivel6);
        textNivel7 = (TextView) view.findViewById(R.id.textNivel7);
        textNivel8 = (TextView) view.findViewById(R.id.textNivel8);
        textNivel9 = (TextView) view.findViewById(R.id.textNivel9);
        textNivel10 = (TextView) view.findViewById(R.id.textNivel10);
        textNivel11 = (TextView) view.findViewById(R.id.textNivel11);
        textNivel12 = (TextView) view.findViewById(R.id.textNivel12);
        textNivel13 = (TextView) view.findViewById(R.id.textNivel13);
        textNivel14 = (TextView) view.findViewById(R.id.textNivel14);
        textNivel15 = (TextView) view.findViewById(R.id.textNivel15);

        imageTrofeo = (ImageView) view.findViewById(R.id.imageTrofeo);

        textAciertos.setText(""+gnr.getLoggedUser().getLevel().getHits_count());
        textNivel.setText(""+ gnr.getLoggedUser().getLevel().getOrder());
        textTituloNivel.setText(""+gnr.getLoggedUser().getLevel().getName());
        textTituloNivel1.setText(""+gnr.getLoggedUser().getLevel().getName());
        textPuntos.setText(""+gnr.getLoggedUser().getLevel().getPoints());



        int nivel = gnr.getLoggedUser().getLevel().getOrder();

        tintLevel(nivel);
        File file = new File(General.local_dir_images + "levels/trof_" + nivel + ".png");
        if(file.exists()){
            options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            imageTrofeo.setImageBitmap(bm);
        }
        return view;
    }

    private void tintLevel(int level){
        int i;
        setActiveTextView(getTextView(level));
        for(i=0; i < level; i++){
            setPassedTextView(getTextView(i));
        }
        for(i=level+1; i <= 15; i++){
            setInActiveTextView(getTextView(i));
        }

    }
    private TextView getTextView(int order){
        switch (order){
            case 0:
                return textNivel0;
            case 1:
                return textNivel1;
            case 2:
                return textNivel2;
            case 3:
                return textNivel3;
            case 4:
                return textNivel4;
            case 5:
                return textNivel5;
            case 6:
                return textNivel6;
            case 7:
                return textNivel7;
            case 8:
                return textNivel8;
            case 9:
                return textNivel9;
            case 10:
                return textNivel10;
            case 11:
                return textNivel11;
            case 12:
                return textNivel12;
            case 13:
                return textNivel13;
            case 14:
                return textNivel14;
            case 15:
                return textNivel15;
            default:
                return textNivel0;
        }
    }
    private void setActiveTextView(TextView textView){
        textView.setBackgroundResource(R.drawable.image_circle_blue_solid_green);
        textView.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
    }
    private void setInActiveTextView(TextView textView){
        textView.setBackgroundResource(R.drawable.image_circle_gray_solid_white);
        textView.setTextColor(ContextCompat.getColor(getActivity(),R.color.mediumGray));
    }
    private void setPassedTextView(TextView textView){
        textView.setBackgroundResource(R.drawable.image_circle_green_solid_blue);
        textView.setTextColor(ContextCompat.getColor(getActivity(),R.color.greenApple));
    }
}
