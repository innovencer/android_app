package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
public class Cuenta_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    TextView textInfo,textNotificaciones,textCuenta, textNombre, textTipoUsuario;
    ImageView imageTipoUsua;
    LinearLayout linear1, linear2, linear3, linearTitular, linearSuplente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        textInfo = (TextView) view.findViewById(R.id.textInfo);
        textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
        textCuenta = (TextView) view.findViewById(R.id.textCuenta);
        textNombre = (TextView) view.findViewById(R.id.textNombre);
        textTipoUsuario = (TextView) view.findViewById(R.id.textTipoUsuario);

        imageTipoUsua = (ImageView) view.findViewById(R.id.imageTipoUsuario);

        linear1 = (LinearLayout) view.findViewById(R.id.linear1);
        linear2 = (LinearLayout) view.findViewById(R.id.linear2);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);
        linearTitular = (LinearLayout) view.findViewById(R.id.linearTitular);
        linearSuplente = (LinearLayout) view.findViewById(R.id.linearSuplente);

        String nombre = "";
        String name[] = gnr.getLoggedUser().getName().split(" ");
        switch (name.length){
            case 1:
                nombre = name[0];
                break;
            case 2:
                nombre = name[0];
                break;
            case 3:
                nombre = name[0]+ " "+name[1];
                break;
            case 4:
                nombre = name[0] +" "+ name[1];
                break;
            default:
                if (name.length>4){
                    nombre = name[0] +" "+ name[1];
                }
                break;
        }

        textNombre.setText(nombre+",");
        textInfo.setOnClickListener(onClickInfo);
        linear1.setOnClickListener(onClickInfo);

        textNotificaciones.setOnClickListener(onClickNotificaciones);
        linear2.setOnClickListener(onClickNotificaciones);

        //textCuenta.setOnClickListener(onClickCuenta);
        //linear3.setOnClickListener(onClickCuenta);

        return view;
    }

    View.OnClickListener onClickInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Perfil_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };
    View.OnClickListener onClickCuenta = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Cuenta_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };
    View.OnClickListener onClickNotificaciones = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Notificaciones_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };
}
