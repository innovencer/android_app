package com.advante.golazzos.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.advante.golazzos.Adapters.List_Input;
import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Ruben Flores on 4/30/2016.
 */
public class FanaticadaEscribir_Fragment extends GeneralFragment {
    EditText editComentarios;
    LinearLayout buttonBack,buttonPublicar, buttonCamera, linearAttached,buttonClearImage,linear1;
    JsonObjectRequest jsArrayRequest;
    ImageView imageAttached, imageEquipo1;
    String pic_name;
    Bitmap bitmapAttached;
    public static Uri path;
    int id, normalHeight = 0;
    InputMethodManager imm;
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
        final View view = inflater.inflate(R.layout.fragment_fanaticada_escribir, container, false);
        if(flag) {
            imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            buttonPublicar = (LinearLayout) view.findViewById(R.id.buttonPublicar);
            buttonCamera = (LinearLayout) view.findViewById(R.id.buttonCamera);
            buttonBack = (LinearLayout) view.findViewById(R.id.buttonBack);
            buttonClearImage = (LinearLayout) view.findViewById(R.id.buttonClearImage);
            linearAttached = (LinearLayout) view.findViewById(R.id.linearAttached);
            linear1 = (LinearLayout) view.findViewById(R.id.linear1);
            editComentarios = (EditText) view.findViewById(R.id.editCometario);
            imageAttached = (ImageView) view.findViewById(R.id.imageAttached);
            imageEquipo1 = (ImageView) view.findViewById(R.id.imageEquipo1);

            showLog(gnr.getLoggedUser().getProfile_pic_url());
            loadProfilePic();
            Bundle bundle = this.getArguments();
            //id = bundle.getInt("id",0);

            buttonPublicar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Comentar();
                }
            });
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
            buttonClearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageAttached.setImageBitmap(null);
                    bitmapAttached = null;
                    linearAttached.setVisibility(View.GONE);
                }
            });
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    getFragmentManager().popBackStack();
                }
            });

            final Resources r = getResources();
            final int dp50 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    50,
                    r.getDisplayMetrics()
            );

            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (normalHeight == 0) {
                        normalHeight = view.getHeight();
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , LinearLayout.LayoutParams.WRAP_CONTENT);

                    if (view.getHeight() < normalHeight) {
                        params.setMargins(0, 0, 0, 0);
                        linear1.setLayoutParams(params);
                    } else if (view.getHeight() == normalHeight) {
                        params.setMargins(0, 0, 0, dp50);
                        linear1.setLayoutParams(params);
                    }

                }
            });
        }
        return view;
    }

    private void loadProfilePic(){
        if(gnr.getLoggedUser().getProfile_pic_url().contains("facebook.com")){
            pic_name = ""+gnr.getLoggedUser().getId();
        }else{
            pic_name = gnr.getLoggedUser().getProfile_pic_url().substring(
                    gnr.getLoggedUser().getProfile_pic_url().lastIndexOf("/"),
                    gnr.getLoggedUser().getProfile_pic_url().lastIndexOf("."));

            if (!gnr.getLoggedUser().getProfile_pic_url().startsWith("http")){
                gnr.getLoggedUser().setProfile_pic_url("http:"+gnr.getLoggedUser().getProfile_pic_url());
            }
        }
        com.advante.golazzos.Helpers.Picasso.with(getContext()).load(gnr.getLoggedUser().getProfile_pic_url()).transform(new CircleTransform()).into(imageEquipo1);
    }
    private void Comentar(){
        dialog.show();
        JSONObject post = new JSONObject();
        JSONObject values = new JSONObject();

        try {
            values.put("text", editComentarios.getText().toString());
            if(bitmapAttached != null){
                String base64 = "data:image/jpeg;base64,"+General.encodeToBase64(bitmapAttached, Bitmap.CompressFormat.JPEG, 100);
                values.put("image_base_64",base64);
            }
            post.put("post",values);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_posts,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data = response.getJSONObject("response");
                            showLog(data.toString());
                            dialog.dismiss();
                            getFragmentManager().popBackStack();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores

                        String body = "";
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if(statusCode != null && statusCode.equals("422")){
                            if(error.networkResponse.data!=null) {
                                try {
                                    body = new String(error.networkResponse.data,"UTF-8");
                                    showShortToast(body);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                                showLog(body);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            showShortToast("Error en la comunicacion, por favor intente mas tarde.");
                        }
                        dialog.dismiss();


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
                        imageAttached.setImageBitmap(bitmap);
                        bitmapAttached = bitmap;
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
                        linearAttached.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        break;
                    case 1:
                        pickImageFromSource(Sources.GALLERY);
                        linearAttached.setVisibility(View.VISIBLE);
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

}
