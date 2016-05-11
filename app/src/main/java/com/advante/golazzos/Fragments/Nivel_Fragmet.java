package com.advante.golazzos.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.R;

/**
 * Created by Ruben Flores on 5/8/2016.
 */
public class Nivel_Fragmet extends GeneralFragment {
    TextView textNivel, textTituloNivel, textAciertos, textPuntos, textTituloNivel1;
    View bar1,bar2,bar3,bar4,bar5,bar6,bar7,bar8,bar9;
    ImageView imageBalon1,imageBalon2,imageBalon3,imageBalon4,imageBalon5,imageBalon6,imageBalon7,imageBalon8,imageBalon9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nivel, container, false);

        textAciertos = (TextView) view.findViewById(R.id.textAciertos);
        textNivel = (TextView) view.findViewById(R.id.textNivel);
        textTituloNivel = (TextView) view.findViewById(R.id.textTituloNivel);
        textPuntos = (TextView) view.findViewById(R.id.textPuntos);
        textTituloNivel1 = (TextView) view.findViewById(R.id.textTituloNivel1);

        textAciertos.setText(""+gnr.getLoggedUser().getLevel().getHits_count());
        textNivel.setText("NIVEL "+gnr.getLoggedUser().getLevel().getOrder());
        textTituloNivel.setText(""+gnr.getLoggedUser().getLevel().getName());
        textTituloNivel1.setText(""+gnr.getLoggedUser().getLevel().getName());
        textPuntos.setText(""+gnr.getLoggedUser().getLevel().getPoints());

        bar1 = view.findViewById(R.id.bar1);
        bar2 = view.findViewById(R.id.bar2);
        bar3 = view.findViewById(R.id.bar3);
        bar4 = view.findViewById(R.id.bar4);
        bar5 = view.findViewById(R.id.bar5);
        bar6 = view.findViewById(R.id.bar6);
        bar7 = view.findViewById(R.id.bar7);
        bar8 = view.findViewById(R.id.bar8);
        bar9 = view.findViewById(R.id.bar9);

        imageBalon1 = (ImageView) view.findViewById(R.id.imageBalon1);
        imageBalon2 = (ImageView) view.findViewById(R.id.imageBalon2);
        imageBalon3 = (ImageView) view.findViewById(R.id.imageBalon3);
        imageBalon4 = (ImageView) view.findViewById(R.id.imageBalon4);
        imageBalon5 = (ImageView) view.findViewById(R.id.imageBalon5);
        imageBalon6 = (ImageView) view.findViewById(R.id.imageBalon6);
        imageBalon7 = (ImageView) view.findViewById(R.id.imageBalon7);
        imageBalon8 = (ImageView) view.findViewById(R.id.imageBalon8);
        imageBalon9 = (ImageView) view.findViewById(R.id.imageBalon9);

        int nivel = gnr.getLoggedUser().getLevel().getOrder() + 1;
        tintBar(nivel);

        return view;
    }

    private void tintBar(int order){
        switch (order){
            case 1:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.colorAccent);
                bar3.setBackgroundResource(R.color.colorAccent);
                bar4.setBackgroundResource(R.color.colorAccent);
                bar5.setBackgroundResource(R.color.colorAccent);
                bar6.setBackgroundResource(R.color.colorAccent);
                bar7.setBackgroundResource(R.color.colorAccent);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.VISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 2:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.colorAccent);
                bar4.setBackgroundResource(R.color.colorAccent);
                bar5.setBackgroundResource(R.color.colorAccent);
                bar6.setBackgroundResource(R.color.colorAccent);
                bar7.setBackgroundResource(R.color.colorAccent);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.VISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 3:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.colorAccent);
                bar5.setBackgroundResource(R.color.colorAccent);
                bar6.setBackgroundResource(R.color.colorAccent);
                bar7.setBackgroundResource(R.color.colorAccent);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.VISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 4:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.colorAccent);
                bar6.setBackgroundResource(R.color.colorAccent);
                bar7.setBackgroundResource(R.color.colorAccent);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.VISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 5:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.greenApple);
                bar6.setBackgroundResource(R.color.colorAccent);
                bar7.setBackgroundResource(R.color.colorAccent);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.VISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 6:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.greenApple);
                bar6.setBackgroundResource(R.color.greenApple);
                bar7.setBackgroundResource(R.color.colorAccent);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.VISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 7:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.greenApple);
                bar6.setBackgroundResource(R.color.greenApple);
                bar7.setBackgroundResource(R.color.greenApple);
                bar8.setBackgroundResource(R.color.colorAccent);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.VISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 8:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.greenApple);
                bar6.setBackgroundResource(R.color.greenApple);
                bar7.setBackgroundResource(R.color.greenApple);
                bar8.setBackgroundResource(R.color.greenApple);
                bar9.setBackgroundResource(R.color.colorAccent);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.VISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
            case 9:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.greenApple);
                bar6.setBackgroundResource(R.color.greenApple);
                bar7.setBackgroundResource(R.color.greenApple);
                bar8.setBackgroundResource(R.color.greenApple);
                bar9.setBackgroundResource(R.color.greenApple);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.VISIBLE);
                break;
            default:
                bar1.setBackgroundResource(R.color.greenApple);
                bar2.setBackgroundResource(R.color.greenApple);
                bar3.setBackgroundResource(R.color.greenApple);
                bar4.setBackgroundResource(R.color.greenApple);
                bar5.setBackgroundResource(R.color.greenApple);
                bar6.setBackgroundResource(R.color.greenApple);
                bar7.setBackgroundResource(R.color.greenApple);
                bar8.setBackgroundResource(R.color.greenApple);
                bar9.setBackgroundResource(R.color.greenApple);

                imageBalon1.setVisibility(View.INVISIBLE);
                imageBalon2.setVisibility(View.INVISIBLE);
                imageBalon3.setVisibility(View.INVISIBLE);
                imageBalon4.setVisibility(View.INVISIBLE);
                imageBalon5.setVisibility(View.INVISIBLE);
                imageBalon6.setVisibility(View.INVISIBLE);
                imageBalon7.setVisibility(View.INVISIBLE);
                imageBalon8.setVisibility(View.INVISIBLE);
                imageBalon9.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
