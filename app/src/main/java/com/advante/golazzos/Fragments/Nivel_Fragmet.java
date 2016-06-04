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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben Flores on 5/8/2016.
 */
public class Nivel_Fragmet extends GeneralFragment {
    TextView textNivel, textTituloNivel, textAciertos, textPuntos, textTituloNivel1;
    TextView textNivel0, textNivel1, textNivel2, textNivel3, textNivel4, textNivel5, textNivel6, textNivel7,
             textNivel8, textNivel9, textNivel10, textNivel11, textNivel12, textNivel13, textNivel14, textNivel15,text1;
    ImageView imageTrofeo, image0, image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12,
                        image13, image14, image15;
    BitmapFactory.Options options;
    int nivel;
    List<ImageView> imageViews = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nivel, container, false);

        textAciertos = (TextView) view.findViewById(R.id.textAciertos);
        textNivel = (TextView) view.findViewById(R.id.textNivel);
        textTituloNivel = (TextView) view.findViewById(R.id.textTituloNivel);
        textPuntos = (TextView) view.findViewById(R.id.textPuntos);
        textTituloNivel1 = (TextView) view.findViewById(R.id.textTituloNivel1);
        text1 = (TextView) view.findViewById(R.id.text1);

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

        image0 = (ImageView) view.findViewById(R.id.image0);
        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        image3 = (ImageView) view.findViewById(R.id.image3);
        image4 = (ImageView) view.findViewById(R.id.image4);
        image5 = (ImageView) view.findViewById(R.id.image5);
        image6 = (ImageView) view.findViewById(R.id.image6);
        image7 = (ImageView) view.findViewById(R.id.image7);
        image8 = (ImageView) view.findViewById(R.id.image8);
        image9 = (ImageView) view.findViewById(R.id.image9);
        image10 = (ImageView) view.findViewById(R.id.image10);
        image11 = (ImageView) view.findViewById(R.id.image11);
        image12 = (ImageView) view.findViewById(R.id.image12);
        image13 = (ImageView) view.findViewById(R.id.image13);
        image14 = (ImageView) view.findViewById(R.id.image14);
        image15 = (ImageView) view.findViewById(R.id.image15);

        textNivel0.setOnClickListener(onClickLevel);
        textNivel1.setOnClickListener(onClickLevel);
        textNivel2.setOnClickListener(onClickLevel);
        textNivel3.setOnClickListener(onClickLevel);
        textNivel4.setOnClickListener(onClickLevel);
        textNivel5.setOnClickListener(onClickLevel);
        textNivel6.setOnClickListener(onClickLevel);
        textNivel7.setOnClickListener(onClickLevel);
        textNivel8.setOnClickListener(onClickLevel);
        textNivel9.setOnClickListener(onClickLevel);
        textNivel10.setOnClickListener(onClickLevel);
        textNivel11.setOnClickListener(onClickLevel);
        textNivel12.setOnClickListener(onClickLevel);
        textNivel13.setOnClickListener(onClickLevel);
        textNivel14.setOnClickListener(onClickLevel);
        textNivel15.setOnClickListener(onClickLevel);

        imageViews.add(image0);
        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
        imageViews.add(image5);
        imageViews.add(image6);
        imageViews.add(image7);
        imageViews.add(image8);
        imageViews.add(image9);
        imageViews.add(image10);
        imageViews.add(image11);
        imageViews.add(image12);
        imageViews.add(image13);
        imageViews.add(image14);
        imageViews.add(image15);

        imageTrofeo = (ImageView) view.findViewById(R.id.imageTrofeo);

        textAciertos.setText(""+gnr.getLoggedUser().getLevel().getHits_count());
        textNivel.setText(""+ gnr.getLoggedUser().getLevel().getOrder());
        textTituloNivel.setText(""+gnr.getLoggedUser().getLevel().getName());
        textTituloNivel1.setText(""+gnr.getLoggedUser().getLevel().getName());
        textPuntos.setText(""+gnr.getLoggedUser().getLevel().getPoints() +" puntos");



        nivel = gnr.getLoggedUser().getLevel().getOrder();

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

    View.OnClickListener onClickLevel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //textTituloNivel.setText(view.getTag().toString().split(";")[0].toUpperCase());
            textTituloNivel1.setText(view.getTag().toString().split(";")[0]);
            textAciertos.setText(view.getTag().toString().split(";")[2].toUpperCase());
            textPuntos.setText(view.getTag().toString().split(";")[1].toUpperCase() +" puntos");
            switch (view.getId()){
                case R.id.textNivel0:
                    tintLevel2(0);
                    break;
                case R.id.textNivel1:
                    tintLevel2(1);
                    break;
                case R.id.textNivel2:
                    tintLevel2(2);
                    break;
                case R.id.textNivel3:
                    tintLevel2(3);
                    break;
                case R.id.textNivel4:
                    tintLevel2(4);
                    break;
                case R.id.textNivel5:
                    tintLevel2(5);
                    break;
                case R.id.textNivel6:
                    tintLevel2(6);
                    break;
                case R.id.textNivel7:
                    tintLevel2(7);
                    break;
                case R.id.textNivel8:
                    tintLevel2(8);
                    break;
                case R.id.textNivel9:
                    tintLevel2(9);
                    break;
                case R.id.textNivel10:
                    tintLevel2(10);
                    break;
                case R.id.textNivel11:
                    tintLevel2(11);
                    break;
                case R.id.textNivel12:
                    tintLevel2(12);
                    break;
                case R.id.textNivel13:
                    tintLevel2(13);
                    break;
                case R.id.textNivel14:
                    tintLevel2(14);
                    break;
                case R.id.textNivel15:
                    tintLevel2(15);
                    break;
                default:
                    tintLevel2(0);
                    break;
            }

        }
    };

    private void tintLevel(int level){
        int i;
        setActiveTextView(getTextView(level));
        for(i=0; i < level; i++){
            setPassedTextView(getTextView(i));
        }
        for(i=level+1; i <= 15; i++){
            setInActiveTextView(getTextView(i));
        }
        for(i=0;i<imageViews.size();i++){
            if(i==level){
                imageViews.get(i).setVisibility(View.VISIBLE);
            }else{
                imageViews.get(i).setVisibility(View.INVISIBLE);
            }
        }

    }
    private void tintLevel2(int levelClick){
        //int i;
        tintLevel(nivel);
        if(nivel<levelClick){
            text1.setText(getString(R.string.text1));
        }else{
            text1.setText("Como recompeza obtuviste");
        }
        setPassedTextView(getTextView(levelClick));
        File file = new File(General.local_dir_images + "levels/trof_" + levelClick + ".png");
        if(file.exists()){
            options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            imageTrofeo.setImageBitmap(bm);
        }
        for(int i=0;i<imageViews.size();i++){
            if(i==levelClick){
                imageViews.get(i).setVisibility(View.VISIBLE);
            }else{
                imageViews.get(i).setVisibility(View.INVISIBLE);
            }
        }
        /*
        for(i=0; i < level; i++){
            setAlpha(getImageView(i),0.5f);
        }
        for(i=level+1; i <= 15; i++){
            setAlpha(getImageView(i),0.5f);
        }
        */
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
