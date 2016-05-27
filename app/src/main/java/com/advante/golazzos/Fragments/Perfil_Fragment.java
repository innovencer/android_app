package com.advante.golazzos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.MyFirebaseInstanceIDService;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.LoginActivity;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.PrincipalActivity;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Perfil_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    EditText editNombre, editApellido, editEmail, editTelefono;
    TextView textInfo,textNotificaciones,textCuenta, textCerrarSesion;
    ImageView imageProfile,imageTipoUsuario;
    LinearLayout linear1, linear2, linear3,linearGuardar;
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
        linearGuardar = (LinearLayout) view.findViewById(R.id.linearGuardar);

        //textInfo.setOnClickListener(onClickInfo);
        //linear1.setOnClickListener(onClickInfo);
        linearGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
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

        File noImage = new File(General.local_dir_images + "profile/no_profile.png");
        File file = new File(General.local_dir_images + "profile/"+ gnr.getLoggedUser().getId() +".png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        GraphicsUtil graphicUtil = new GraphicsUtil();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm;
        if(file.exists())
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        else{
            bm = BitmapFactory.decodeFile(noImage.getAbsolutePath(), options);
        }
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

    private void save() {
        JSONObject user = new JSONObject();
        JSONObject values = new JSONObject();
        try {
            values.put("first_name", editNombre.getText().toString());
            values.put("last_name", editApellido.getText().toString());
            values.put("email", editEmail.getText().toString());
            //values.put("phone", device_registration_id);

            user.put("user",values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                General.endpoint_users+"/me",
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        gnr.getLoggedUser().setName(editNombre.getText().toString()+" "+editApellido.getText().toString());
                        Toast.makeText(getContext(),"Perfil Actualizado ...",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error en al tratar de conectar con el servicio web. Intente mas tarde",Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + General.getToken());
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }
}
