package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.R;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Golazzos_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    TextView textInfo,textNotificaciones,textCuenta;
    LinearLayout linear1, linear2, linear3;
    ImageView image1, image2, image3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_golazzos, container, false);

        textInfo = (TextView) view.findViewById(R.id.textInfo);
        textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
        textCuenta = (TextView) view.findViewById(R.id.textCuenta);

        linear1 = (LinearLayout) view.findViewById(R.id.linear1);
        linear2 = (LinearLayout) view.findViewById(R.id.linear2);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);

        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        image3 = (ImageView) view.findViewById(R.id.image3);

        textInfo.setOnClickListener(onClickInfo);
        linear1.setOnClickListener(onClickInfo);

        textNotificaciones.setOnClickListener(onClickNotificaciones);
        linear2.setOnClickListener(onClickNotificaciones);

        textCuenta.setOnClickListener(onClickCuenta);
        linear3.setOnClickListener(onClickCuenta);

        return view;
    }

    View.OnClickListener onClickInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.INVISIBLE);
        }
    };
    View.OnClickListener onClickCuenta = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.VISIBLE);
        }
    };
    View.OnClickListener onClickNotificaciones = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.INVISIBLE);
        }
    };
}
