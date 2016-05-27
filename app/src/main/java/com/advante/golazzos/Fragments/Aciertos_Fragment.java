package com.advante.golazzos.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Users;
import com.advante.golazzos.Helpers.Bar;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.Model.UserBusqueda;
import com.advante.golazzos.Model.UserLevel;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Aciertos_Fragment extends GeneralFragment {
    TextView textTotalJugados, textAciertos, textPerdidos, textPorcentajeGanados, textPorcentajePerdidos,
             textJugados1, textAciertos1, textEfectividad1, textJugados2, textAciertos2, textEfectividad2;
    ImageView imageProfile;
    Bar barWin1, barLose;
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
        imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
        barWin1 = (Bar) view.findViewById(R.id.barWin1);
        barLose = (Bar) view.findViewById(R.id.barLose);

        textTotalJugados.setText(gnr.getLoggedUser().getCounters().getTotalBets() +" JUGADAS");
        textAciertos.setText(gnr.getLoggedUser().getCounters().getTotalWonBets()  +" GANADAS");
        textPerdidos.setText(gnr.getLoggedUser().getCounters().getTotalLoseBets() +" PERDIDAS");

        int porcentajeGanados = Math.round((gnr.getLoggedUser().getCounters().getTotalWonBets() / gnr.getLoggedUser().getCounters().getTotalBets()) * 100);
        textPorcentajeGanados.setText(porcentajeGanados +"%");
        barWin1.setPorcentaje(porcentajeGanados);
        int colors[] = new int[2];
        colors[0] = ContextCompat.getColor(getContext(), R.color.greenApple);
        colors[1] = ContextCompat.getColor(getContext(), R.color.colorAccent);
        barWin1.setColors(colors);

        int porcentajePerdidos = Math.round((gnr.getLoggedUser().getCounters().getTotalLoseBets() / gnr.getLoggedUser().getCounters().getTotalBets()) * 100);
        textPorcentajePerdidos.setText(porcentajePerdidos +"%");
        barLose.setPorcentaje(porcentajePerdidos);
        colors[0] = Color.RED;
        colors[1] = ContextCompat.getColor(getContext(), R.color.colorAccent);
        barLose.setColors(colors);

        textJugados1.setText(""+gnr.getLoggedUser().getCounters().getMarcadorTotalBets());
        textAciertos1.setText(""+gnr.getLoggedUser().getCounters().getMarcadorWonBets());
        int porcentajeGanados1=0;
        if(gnr.getLoggedUser().getCounters().getMarcadorWonBets()>0){
            porcentajeGanados1 = Math.round((gnr.getLoggedUser().getCounters().getMarcadorWonBets() / gnr.getLoggedUser().getCounters().getMarcadorTotalBets()) * 100);
        }
        textEfectividad1.setText(porcentajeGanados1+"%");

        textJugados2.setText(""+gnr.getLoggedUser().getCounters().getGanaPierdeTotalBets());
        textAciertos2.setText(""+gnr.getLoggedUser().getCounters().getGanaPierdeWonBets());
        int porcentajeGanados2 = 0;
        if (gnr.getLoggedUser().getCounters().getGanaPierdeWonBets() > 0) {
            porcentajeGanados2 = Math.round((gnr.getLoggedUser().getCounters().getGanaPierdeWonBets() / gnr.getLoggedUser().getCounters().getGanaPierdeTotalBets()) * 100);
        }
        textEfectividad2.setText(porcentajeGanados2+"%");

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
