package com.advante.golazzos.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Input;
import com.advante.golazzos.Helpers.API;
import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.JSONBuilder;
import com.advante.golazzos.Helpers.Picasso;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.LoginActivity;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.PrincipalActivity;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Perfil_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    EditText editNombre, editApellido, editEmail, editTelefono;
    TextView textInfo,textNotificaciones,textCuenta, textCerrarSesion, textTipoUsuario, textSetPassword;
    ImageView imageProfile,imageTipoUsuario,imageEditar;
    LinearLayout linear1, linear2, linear3,linearGuardar;
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        if(flag) {
            editNombre = (EditText) view.findViewById(R.id.editNombre);
            editApellido = (EditText) view.findViewById(R.id.editApellido);
            editEmail = (EditText) view.findViewById(R.id.editEmail);
            editTelefono = (EditText) view.findViewById(R.id.editTelefono);
            imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
            imageEditar = (ImageView) view.findViewById(R.id.imageEditar);
            imageTipoUsuario = (ImageView) view.findViewById(R.id.imageTipoUsuario);

            textInfo = (TextView) view.findViewById(R.id.textInfo);
            textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
            textCuenta = (TextView) view.findViewById(R.id.textCuenta);
            textCerrarSesion = (TextView) view.findViewById(R.id.textCerrarSesion);
            textTipoUsuario = (TextView) view.findViewById(R.id.textTipoUsuario);
            textSetPassword = (TextView) view.findViewById(R.id.textSetPassword);

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

            textCuenta.setOnClickListener(onClickCuenta);
            linear3.setOnClickListener(onClickCuenta);
            textCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginManager.getInstance().logOut();
                    preferences.edit().putString("token", "").apply();
                    gnr.setLoggedUser(null);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
            textSetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPassowrd();
                }
            });
            imageEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });

            gnr.getUser(new IGetUser_Listener() {
                @Override
                public void onComplete(Boolean complete, User user) {
                    init();
                }
            });
        }
        return view;
    }

    private void init(){
        String nombre = "", apellido = "";

        String name[];
        if(gnr.getLoggedUser().getName() != null){
            name = gnr.getLoggedUser().getName().split(" ");
        }else{
            name = "Error Error".split(" ");
        }
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
            if(gnr.getLoggedUser().getPaid_subscription()){
                textTipoUsuario.setText("Titular");
                imageTipoUsuario.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.titular_icono));
            }else{
                textTipoUsuario.setText("Suplente");
                imageTipoUsuario.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.suplente_icono));
            }
        }

        Picasso.with(getContext()).load(gnr.getLoggedUser().getProfile_pic_url()).transform(new CircleTransform()).into(imageProfile);

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
                params.put("Authorization", "Token " + gnr.getToken());
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

    private void pickImageFromSource(Sources source) {
        RxImagePicker.with(getContext()).requestImage(source)
                .flatMap(new Func1<Uri, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(Uri uri) {
                        return RxImageConverters.uriToBitmap(getContext(), uri);
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        // Do something with Bitmap
                        imageProfile.setImageBitmap(bitmap);
                        API.getInstance(getContext()).authenticateObjectRequest(Request.Method.PUT, gnr.endpoint_users + "/me",
                                JSONBuilder.changeAvatar(bitmap),
                                new API_Listener() {
                                    @Override
                                    public void OnSuccess(JSONObject response) {

                                    }

                                    @Override
                                    public void OnSuccess(JSONArray response) {

                                    }

                                    @Override
                                    public void OnError(VolleyError error) {

                                    }
                                });
                    }
                });
    }

    private void selectImage() {
        final String[] items = { "Camara", "Galeria", "Cancelar" };
        final Dialog dialog = new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input);

        List_Input adapter = new List_Input(getContext(), Arrays.asList(items));
        ListView listView = (ListView) dialog.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        pickImageFromSource(Sources.CAMERA);
                        dialog.dismiss();
                        break;
                    case 1:
                        pickImageFromSource(Sources.GALLERY);
                        dialog.dismiss();
                        break;
                    case 2:
                        dialog.dismiss();
                }
            }
        });
        listView.setAdapter(adapter);
        dialog.show();
    }

    private void setPassowrd(){
        final Dialog dialog1 = new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Dialog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_password_3);
        final EditText password1 = (EditText) dialog1.findViewById(R.id.editPassword);
        final EditText password2 = (EditText) dialog1.findViewById(R.id.editPassword2);
        LinearLayout btnAceptar = (LinearLayout) dialog1.findViewById(R.id.btnAceptar);
        LinearLayout btnCancelar = (LinearLayout) dialog1.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password1.getText().toString().length()>0 && password1.getText().toString().equals(password2.getText().toString())){
                    dialog.show();
                    JSONObject password = new JSONObject();
                    JSONObject data = new JSONObject();

                    try {
                        //data.put("reset_password_token", token.getText().toString());
                        data.put("password", password1.getText().toString());
                        //data.put("password_confirmation", password2.getText().toString());
                        password.put("user", data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showLog(password.toString());
                    jsArrayRequest = new JsonObjectRequest(
                            Request.Method.PUT,
                            General.endpoint_users+"/me",
                            password,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    dialog.dismiss();
                                    dialog1.dismiss();
                                    showShortToast("Su clave se ha cambiado exitosamente.");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(),new String(error.networkResponse.data),Toast.LENGTH_SHORT).show();
                                }
                            }){

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Authorization", "Token "+ gnr.getToken());
                            params.put("Content-Type", "application/json");
                            return params;
                        }
                    };
                    jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                            7000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
                }else{
                    Toast.makeText(getContext(),"Error, revise los campos e intente nuevamente.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog1.show();
    }

}
