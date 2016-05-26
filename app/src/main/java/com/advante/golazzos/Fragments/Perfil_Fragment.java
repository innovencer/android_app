package com.advante.golazzos.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.LoginActivity;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.PrincipalActivity;
import com.advante.golazzos.R;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;

import java.io.File;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Perfil_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    EditText editNombre, editApellido, editEmail, editTelefono;
    TextView textInfo,textNotificaciones,textCuenta, textCerrarSesion;
    ImageView imageProfile,imageTipoUsuario;
    LinearLayout linear1, linear2, linear3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        editNombre = (EditText) view.findViewById(R.id.editNombre);
        editApellido = (EditText) view.findViewById(R.id.editApellido);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editTelefono = (EditText) view.findViewById(R.id.editTelefono);
        imageProfile = (ImageView) view.findViewById(R.id.imageProfile);

        textInfo = (TextView) view.findViewById(R.id.textInfo);
        textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
        textCuenta = (TextView) view.findViewById(R.id.textCuenta);
        textCerrarSesion = (TextView) view.findViewById(R.id.textCerrarSesion);

        linear1 = (LinearLayout) view.findViewById(R.id.linear1);
        linear2 = (LinearLayout) view.findViewById(R.id.linear2);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);

        //textInfo.setOnClickListener(onClickInfo);
        //linear1.setOnClickListener(onClickInfo);

        textNotificaciones.setOnClickListener(onClickNotificaciones);
        linear2.setOnClickListener(onClickNotificaciones);

        textCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                preferences.edit().putString("token","").apply();
                gnr.setLoggedUser(null);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        textCuenta.setOnClickListener(onClickCuenta);
        linear3.setOnClickListener(onClickCuenta);

        String nombre = "", apellido = "";
        String name[] = gnr.getLoggedUser().getName().split(" ");
        switch (name.length){
            case 1:
                nombre = name[0];
                apellido = "";
                break;
            case 2:
                nombre = name[0];
                apellido = name[1];
                break;
            case 3:
                nombre = name[0]+ " "+name[1];
                apellido = name[2];
                break;
            case 4:
                nombre = name[0] +" "+ name[1];
                apellido = name[2] +" "+ name[3];
                break;
            default:
                if (name.length>4){
                    nombre = name[0] +" "+ name[1];
                    apellido = name[2] +" "+ name[3];
                }
                break;
        }
        if(gnr.getLoggedUser() != null){
            editNombre.setText(nombre);
            editApellido.setText(apellido);
            editEmail.setText(gnr.getLoggedUser().getEmail());
        }

        File file = new File(General.local_dir_images + "profile/"+ gnr.getLoggedUser().getId() +".png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        GraphicsUtil graphicUtil = new GraphicsUtil();
        imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));
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
