package com.advante.golazzos.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    LinearLayout buttonPublicar, buttonCamera, linearAttached,buttonClearImage;
    JsonObjectRequest jsArrayRequest;
    ImageView imageAttached, imageEquipo1;
    String pic_name;
    Bitmap bitmapAttached;
    public static Uri path;
    int id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fanaticada_escribir, container, false);

        buttonPublicar = (LinearLayout) view.findViewById(R.id.buttonPublicar);
        buttonCamera = (LinearLayout) view.findViewById(R.id.buttonCamera);
        buttonClearImage = (LinearLayout) view.findViewById(R.id.buttonClearImage);
        linearAttached = (LinearLayout) view.findViewById(R.id.linearAttached);
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


        File file = new File(General.local_dir_images + "profile/" + pic_name + ".png");
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Log.d("profile ", file.getAbsolutePath());
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        } else {
            Picasso.with(getContext())
                    .load(gnr.getLoggedUser().getProfile_pic_url())
                    .into(target);
        }
    }
    private void Comentar(){
        dialog.show();
        JSONObject post = new JSONObject();
        JSONObject values = new JSONObject();

        try {
            values.put("text", editComentarios.getText().toString());
            if(bitmapAttached != null){
                String base64 = "data:image/jpeg;base64,"+encodeToBase64(bitmapAttached, Bitmap.CompressFormat.JPEG, 100);
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
        final CharSequence[] items = { "Camara", "Galeria", "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccionar");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camara")) {
                    pickImageFromSource(Sources.CAMERA);
                    linearAttached.setVisibility(View.VISIBLE);
                } else if (items[item].equals("Galeria")) {
                    pickImageFromSource(Sources.GALLERY);
                    linearAttached.setVisibility(View.VISIBLE);
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Bitmap bm = bitmap;
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
            FileOutputStream stream = null;
            File file;
            try {
                file = new File(General.local_dir_images + "profile/");
                if(!file.exists()){
                    file.mkdir();
                }
                stream = new FileOutputStream(General.local_dir_images + "profile/"+pic_name+".png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    };

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
