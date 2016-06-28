package com.advante.golazzos.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.BarLose;
import com.advante.golazzos.Helpers.BarWin;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.R;

import java.io.File;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Aciertos_Fragment extends GeneralFragment {
    TextView textTotalJugados, textAciertos, textPerdidos, textPorcentajeGanados, textPorcentajePerdidos,
             textJugados1, textAciertos1, textEfectividad1, textJugados2, textAciertos2, textEfectividad2,
             textJugados3, textAciertos3, textEfectividad3, textJugados4, textAciertos4, textEfectividad4,
             textJugados5, textAciertos5, textEfectividad5;
    ImageView imageProfile;
    LinearLayout linearDiferenciaGoles, linearPrimerGol, linearNumeroGoles;
    BarWin barWin1;
    BarLose barLose;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aciertos, container, false);

        textTotalJugados = (TextView) view.findViewById(R.id.textTotalJugados);
        textAciertos = (TextView) view.findViewById(R.id.textAciertos);
        textPerdidos = (TextView) view.findViewById(R.id.textPerdidos);
        textPorcentajeGanados = (TextView) view.findViewById(R.id.textPorcentajeGanados);
        textPorcentajePerdidos = (TextView) view.findViewById(R.id.textPorcentajePerdidos);
        textJugados1 = (TextView) view.findViewById(R.id.textJugados1);
        textAciertos1 = (TextView) view.findViewById(R.id.textAciertos1);
        textEfectividad1 = (TextView) view.findViewById(R.id.textEfectividad1);
        textJugados2 = (TextView) view.findViewById(R.id.textJugados2);
        textAciertos2 = (TextView) view.findViewById(R.id.textAciertos2);
        textEfectividad2 = (TextView) view.findViewById(R.id.textEfectividad2);
        textJugados3 = (TextView) view.findViewById(R.id.textJugados3);
        textAciertos3 = (TextView) view.findViewById(R.id.textAciertos3);
        textEfectividad3 = (TextView) view.findViewById(R.id.textEfectividad3);
        textJugados4 = (TextView) view.findViewById(R.id.textJugados4);
        textAciertos4 = (TextView) view.findViewById(R.id.textAciertos4);
        textEfectividad4 = (TextView) view.findViewById(R.id.textEfectividad4);
        textJugados5 = (TextView) view.findViewById(R.id.textJugados5);
        textAciertos5 = (TextView) view.findViewById(R.id.textAciertos5);
        textEfectividad5 = (TextView) view.findViewById(R.id.textEfectividad5);

        linearDiferenciaGoles = (LinearLayout) view.findViewById(R.id.linearDiferenciaGoles);
        linearPrimerGol = (LinearLayout) view.findViewById(R.id.linearPrimerGol);
        linearNumeroGoles = (LinearLayout) view.findViewById(R.id.linearNumeroGoles);

        imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
        barWin1 = (BarWin) view.findViewById(R.id.barWin1);
        barLose = (BarLose) view.findViewById(R.id.barLose);

        textTotalJugados.setText(gnr.getLoggedUser().getCounters().getTotalBets() +" JUGADAS");
        textAciertos.setText(gnr.getLoggedUser().getCounters().getTotalWonBets()  +" GANADAS");
        textPerdidos.setText(gnr.getLoggedUser().getCounters().getTotalLoseBets() +" PERDIDAS");

        int porcentajeGanados = 0;
        if(gnr.getLoggedUser().getCounters().getTotalWonBets()>0) {
            porcentajeGanados = Math.round(((float)gnr.getLoggedUser().getCounters().getTotalWonBets() / gnr.getLoggedUser().getCounters().getTotalBets()) * 100);
        }
        textPorcentajeGanados.setText(porcentajeGanados +"%");
        barWin1.setPorcentaje(Math.round(porcentajeGanados));

        int porcentajePerdidos = 0;
        if(gnr.getLoggedUser().getCounters().getTotalLoseBets()>0) {
            porcentajePerdidos = Math.round(((float)gnr.getLoggedUser().getCounters().getTotalLoseBets() / (float)gnr.getLoggedUser().getCounters().getTotalBets()) * 100);
        }

        textPorcentajePerdidos.setText(porcentajePerdidos +"%");
        barLose.setPorcentaje(porcentajePerdidos);


        textJugados1.setText(""+gnr.getLoggedUser().getCounters().getMarcadorTotalBets());
        textAciertos1.setText(""+gnr.getLoggedUser().getCounters().getMarcadorWonBets());
        int porcentajeGanados1=0;
        if(gnr.getLoggedUser().getCounters().getMarcadorWonBets()>0){
            porcentajeGanados1 = Math.round(((float)gnr.getLoggedUser().getCounters().getMarcadorWonBets() / (float)gnr.getLoggedUser().getCounters().getMarcadorTotalBets()) * 100);
        }
        textEfectividad1.setText(porcentajeGanados1+"%");

        textJugados2.setText(""+gnr.getLoggedUser().getCounters().getGanaPierdeTotalBets());
        textAciertos2.setText(""+gnr.getLoggedUser().getCounters().getGanaPierdeWonBets());
        int porcentajeGanados2 = 0;
        if (gnr.getLoggedUser().getCounters().getGanaPierdeWonBets() > 0) {
            porcentajeGanados2 = Math.round(((float)gnr.getLoggedUser().getCounters().getGanaPierdeWonBets() / (float)gnr.getLoggedUser().getCounters().getGanaPierdeTotalBets()) * 100);
        }
        textEfectividad2.setText(porcentajeGanados2+"%");

        if(gnr.getLoggedUser().getCounters().getDiferenciaGolesTotalBets()>0){
            linearDiferenciaGoles.setVisibility(View.VISIBLE);
            textJugados3.setText(""+gnr.getLoggedUser().getCounters().getDiferenciaGolesTotalBets());
            textAciertos3.setText(""+gnr.getLoggedUser().getCounters().getDiferenciaGolesWonBets());
            int porcentajeGanados3 = 0;
            if(gnr.getLoggedUser().getCounters().getDiferenciaGolesWonBets()>0){
                porcentajeGanados3 = Math.round(((float)gnr.getLoggedUser().getCounters().getDiferenciaGolesWonBets() / (float)gnr.getLoggedUser().getCounters().getDiferenciaGolesTotalBets()) * 100);
            }
            textEfectividad3.setText(porcentajeGanados3+"%");
        }

        if(gnr.getLoggedUser().getCounters().getPrimerGolTotalBets()>0){
            linearPrimerGol.setVisibility(View.VISIBLE);
            textJugados4.setText(""+gnr.getLoggedUser().getCounters().getPrimerGolTotalBets());
            textAciertos4.setText(""+gnr.getLoggedUser().getCounters().getPrimerGolWonBets());
            int porcentajeGanados4 = 0;
            if(gnr.getLoggedUser().getCounters().getPrimerGolWonBets()>0){
                porcentajeGanados4 = Math.round(((float)gnr.getLoggedUser().getCounters().getPrimerGolWonBets() / (float)gnr.getLoggedUser().getCounters().getPrimerGolTotalBets()) * 100);
            }
            textEfectividad4.setText(porcentajeGanados4+"%");
        }

        if(gnr.getLoggedUser().getCounters().getNumeroGolesTotalBets()>0){
            linearNumeroGoles.setVisibility(View.VISIBLE);
            textJugados5.setText(""+gnr.getLoggedUser().getCounters().getNumeroGolesTotalBets());
            textAciertos5.setText(""+gnr.getLoggedUser().getCounters().getNumeroGolesWonBets());
            int porcentajeGanados5 = 0;
            if(gnr.getLoggedUser().getCounters().getNumeroGolesWonBets()>0){
                porcentajeGanados5 = Math.round(((float)gnr.getLoggedUser().getCounters().getNumeroGolesWonBets() / (float)gnr.getLoggedUser().getCounters().getNumeroGolesTotalBets()) * 100);
            }
            textEfectividad5.setText(porcentajeGanados5+"%");
        }

        File noImage = new File(General.local_dir_images + "profile/no_profile.png");
        File file = new File(General.local_dir_images + "profile/"+ gnr.getLoggedUser().getId() +".png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm;
        if (file.exists()) {
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            final GraphicsUtil graphicUtil = new GraphicsUtil();
            imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }else{
            bm = BitmapFactory.decodeFile(noImage.getAbsolutePath(), options);
            final GraphicsUtil graphicUtil = new GraphicsUtil();
            imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }


        return view;
    }



}
