package com.advante.golazzos.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.LoginActivity;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.R;
import com.advante.golazzos.Wizzard3Activity;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Golazzos_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    TextView textInfo,textNotificaciones,textCuenta;
    LinearLayout linear1, linear2, linear3, linearTerminios, linearNostros, linearButton1;
    RelativeLayout linearContacto;
    ImageView image1, image2, image3;
    Boolean flag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            flag = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_golazzos, container, false);
        if(flag) {
            textInfo = (TextView) view.findViewById(R.id.textInfo);
            textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
            textCuenta = (TextView) view.findViewById(R.id.textCuenta);

            linear1 = (LinearLayout) view.findViewById(R.id.linear1);
            linear2 = (LinearLayout) view.findViewById(R.id.linear2);
            linear3 = (LinearLayout) view.findViewById(R.id.linear3);
            linearTerminios = (LinearLayout) view.findViewById(R.id.linearTerminos);
            linearNostros = (LinearLayout) view.findViewById(R.id.linearNosotros);
            linearContacto = (RelativeLayout) view.findViewById(R.id.linearContacto);
            linearButton1 = (LinearLayout) view.findViewById(R.id.linearButton1);

            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);

            textInfo.setOnClickListener(onClickInfo);
            linear1.setOnClickListener(onClickInfo);

            textNotificaciones.setOnClickListener(onClickNotificaciones);
            linear2.setOnClickListener(onClickNotificaciones);

            textCuenta.setOnClickListener(onClickCuenta);
            linear3.setOnClickListener(onClickCuenta);

            linearButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.emailContacto)});
                    i.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    i.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    try {
                        startActivity(Intent.createChooser(i, "Contactar a Golazzos"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return view;
    }

    View.OnClickListener onClickInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.INVISIBLE);
            linearTerminios.setVisibility(View.VISIBLE);
            linearNostros.setVisibility(View.GONE);
            linearContacto.setVisibility(View.GONE);
        }
    };
    View.OnClickListener onClickCuenta = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.VISIBLE);
            linearTerminios.setVisibility(View.GONE);
            linearNostros.setVisibility(View.GONE);
            linearContacto.setVisibility(View.VISIBLE);
        }
    };
    View.OnClickListener onClickNotificaciones = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.INVISIBLE);
            linearTerminios.setVisibility(View.GONE);
            linearNostros.setVisibility(View.VISIBLE);
            linearContacto.setVisibility(View.GONE);
        }
    };
}
