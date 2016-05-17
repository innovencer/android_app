package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.R;

/**
 * Created by Ruben Flores on 5/17/2016.
 */
public class Trofeos_Fragment extends GeneralFragment {
    ImageView imageTrofeo0,imageTrofeo1,imageTrofeo2,imageTrofeo3,imageTrofeo4,imageTrofeo5,imageTrofeo6,
              imageTrofeo7,imageTrofeo8,imageTrofeo9,imageTrofeo10,imageTrofeo11,imageTrofeo12,imageTrofeo13,
              imageTrofeo14,imageTrofeo15;
    TextView textTituloNivel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trofeos, container, false);

        imageTrofeo0 = (ImageView) view.findViewById(R.id.imageTrofeo0);
        imageTrofeo1 = (ImageView) view.findViewById(R.id.imageTrofeo1);
        imageTrofeo2 = (ImageView) view.findViewById(R.id.imageTrofeo2);
        imageTrofeo3 = (ImageView) view.findViewById(R.id.imageTrofeo3);
        imageTrofeo4 = (ImageView) view.findViewById(R.id.imageTrofeo4);
        imageTrofeo5 = (ImageView) view.findViewById(R.id.imageTrofeo5);
        imageTrofeo6 = (ImageView) view.findViewById(R.id.imageTrofeo6);
        imageTrofeo7 = (ImageView) view.findViewById(R.id.imageTrofeo7);
        imageTrofeo8 = (ImageView) view.findViewById(R.id.imageTrofeo8);
        imageTrofeo9 = (ImageView) view.findViewById(R.id.imageTrofeo9);
        imageTrofeo10 = (ImageView) view.findViewById(R.id.imageTrofeo10);
        imageTrofeo11 = (ImageView) view.findViewById(R.id.imageTrofeo11);
        imageTrofeo12 = (ImageView) view.findViewById(R.id.imageTrofeo12);
        imageTrofeo13 = (ImageView) view.findViewById(R.id.imageTrofeo13);
        imageTrofeo14 = (ImageView) view.findViewById(R.id.imageTrofeo14);
        imageTrofeo15 = (ImageView) view.findViewById(R.id.imageTrofeo15);

        imageTrofeo0.setOnClickListener(onClickTrofeo);
        imageTrofeo1.setOnClickListener(onClickTrofeo);
        imageTrofeo2.setOnClickListener(onClickTrofeo);
        imageTrofeo3.setOnClickListener(onClickTrofeo);
        imageTrofeo4.setOnClickListener(onClickTrofeo);
        imageTrofeo5.setOnClickListener(onClickTrofeo);
        imageTrofeo6.setOnClickListener(onClickTrofeo);
        imageTrofeo7.setOnClickListener(onClickTrofeo);
        imageTrofeo8.setOnClickListener(onClickTrofeo);
        imageTrofeo9.setOnClickListener(onClickTrofeo);
        imageTrofeo10.setOnClickListener(onClickTrofeo);
        imageTrofeo11.setOnClickListener(onClickTrofeo);
        imageTrofeo12.setOnClickListener(onClickTrofeo);
        imageTrofeo13.setOnClickListener(onClickTrofeo);
        imageTrofeo14.setOnClickListener(onClickTrofeo);
        imageTrofeo15.setOnClickListener(onClickTrofeo);

        textTituloNivel = (TextView) view.findViewById(R.id.textTituloNivel);

        int nivel = gnr.getLoggedUser().getLevel().getOrder();

        tintLevel(nivel);

        return view;
    }

    View.OnClickListener onClickTrofeo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textTituloNivel.setText(view.getTag().toString().toUpperCase());
            switch (view.getId()){
                case R.id.imageTrofeo0:
                    tintLevel2(0);
                    break;
                case R.id.imageTrofeo1:
                    tintLevel2(1);
                    break;
                case R.id.imageTrofeo2:
                    tintLevel2(2);
                    break;
                case R.id.imageTrofeo3:
                    tintLevel2(3);
                    break;
                case R.id.imageTrofeo4:
                    tintLevel2(4);
                    break;
                case R.id.imageTrofeo5:
                    tintLevel2(5);
                    break;
                case R.id.imageTrofeo6:
                    tintLevel2(6);
                    break;
                case R.id.imageTrofeo7:
                    tintLevel2(7);
                    break;
                case R.id.imageTrofeo8:
                    tintLevel2(8);
                    break;
                case R.id.imageTrofeo9:
                    tintLevel2(9);
                    break;
                case R.id.imageTrofeo10:
                    tintLevel2(10);
                    break;
                case R.id.imageTrofeo11:
                    tintLevel2(11);
                    break;
                case R.id.imageTrofeo12:
                    tintLevel2(12);
                    break;
                case R.id.imageTrofeo13:
                    tintLevel2(13);
                    break;
                case R.id.imageTrofeo14:
                    tintLevel2(14);
                    break;
                case R.id.imageTrofeo15:
                    tintLevel2(15);
                    break;
                default:
                    tintLevel2(0);
                    break;
            }

        }
    };

    private ImageView getImageView(int order){
        switch (order){
            case 0:
                return imageTrofeo0;
            case 1:
                return imageTrofeo1;
            case 2:
                return imageTrofeo2;
            case 3:
                return imageTrofeo3;
            case 4:
                return imageTrofeo4;
            case 5:
                return imageTrofeo5;
            case 6:
                return imageTrofeo6;
            case 7:
                return imageTrofeo7;
            case 8:
                return imageTrofeo8;
            case 9:
                return imageTrofeo9;
            case 10:
                return imageTrofeo10;
            case 11:
                return imageTrofeo11;
            case 12:
                return imageTrofeo12;
            case 13:
                return imageTrofeo13;
            case 14:
                return imageTrofeo14;
            case 15:
                return imageTrofeo15;
            default:
                return imageTrofeo0;
        }
    }

    private void tintLevel(int level){
        int i;
        for(i=0; i <= level; i++){
            setAlpha(getImageView(i),1);
        }
        for(i=level+1; i <= 15; i++){
            setAlpha(getImageView(i),0.5f);
        }
    }

    private void tintLevel2(int level){
        int i;
        setAlpha(getImageView(level),1);
        for(i=0; i < level; i++){
            setAlpha(getImageView(i),0.5f);
        }
        for(i=level+1; i <= 15; i++){
            setAlpha(getImageView(i),0.5f);
        }
    }

    private void setAlpha(ImageView imageView, float alpha){
        imageView.setAlpha(alpha);
    }

}
