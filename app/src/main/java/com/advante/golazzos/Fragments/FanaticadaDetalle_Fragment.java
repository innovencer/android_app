package com.advante.golazzos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Comentarios;
import com.advante.golazzos.EstadisticasActivity;
import com.advante.golazzos.Helpers.API;
import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.Comentario;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruben Flores on 4/24/2016.
 */
public class FanaticadaDetalle_Fragment extends GeneralFragment {
    LinearLayout buttonBack,buttonFacebook,buttonTwitter,buttonComentar,buttonLike,buttonEstadisticas,linearJugar,linearImageAttached,linear1;
    TextView textTime_ago,textLabel,textLike, buttonPublicar;
    EditText editComentario;
    ImageView imageEquipo1, imageViewAttached;
    CallbackManager callbackManager;
    String label,html_center_url,trackable_type, imageAttached;
    String pic_name,idPartido;
    int id,idImage, idLike = -1, normalHeight = 0;
    InputMethodManager imm;
    ListView listView;
    JsonObjectRequest jsArrayRequest;

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
        final View view = inflater.inflate(R.layout.fragment_fanaticada_detalle, container, false);
        if(flag) {
            callbackManager = CallbackManager.Factory.create();

            buttonBack = (LinearLayout) view.findViewById(R.id.buttonBack);
            buttonFacebook = (LinearLayout) view.findViewById(R.id.buttonFacebook);
            buttonLike = (LinearLayout) view.findViewById(R.id.buttonLike);
            buttonEstadisticas = (LinearLayout) view.findViewById(R.id.buttonEstadisticas);
            buttonComentar = (LinearLayout) view.findViewById(R.id.buttonComentar);
            buttonTwitter = (LinearLayout) view.findViewById(R.id.buttonTwitter);
            buttonPublicar = (TextView) view.findViewById(R.id.buttonPublicar);
            linearJugar = (LinearLayout) view.findViewById(R.id.linearJugar);
            linearImageAttached = (LinearLayout) view.findViewById(R.id.linearAttached);
            linear1 = (LinearLayout) view.findViewById(R.id.linear1);
            textLabel = (TextView) view.findViewById(R.id.textLabel);
            textTime_ago = (TextView) view.findViewById(R.id.textTime_ago);
            textLike = (TextView) view.findViewById(R.id.textLike);
            imageEquipo1 = (ImageView) view.findViewById(R.id.imageEquipo1);
            imageViewAttached = (ImageView) view.findViewById(R.id.imageAttached);
            editComentario = (EditText) view.findViewById(R.id.editCometario);
            listView = (ListView) view.findViewById(R.id.listview);

            Bundle bundle = this.getArguments();
            label = bundle.getString("label", "");
            html_center_url = bundle.getString("html_center_url", "");
            trackable_type = bundle.getString("trackable_type", "");
            imageAttached = bundle.getString("imageAttached", "");
            idPartido = bundle.getString("idPartido", "");
            id = bundle.getInt("id", 0);
            idImage = bundle.getInt("idImage", 0);
            idLike = bundle.getInt("like", -1);

            textLabel.setText(Html.fromHtml(label), TextView.BufferType.SPANNABLE);
            textTime_ago.setText(bundle.getString("time_ago", ""));
            if (idLike > 0) {
                textLike.setText("No me gusta");
            } else {
                textLike.setText("Me gusta");
            }
            if (html_center_url.isEmpty()) {
                buttonEstadisticas.setVisibility(View.GONE);
            } else {
                buttonEstadisticas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), EstadisticasActivity.class);
                        intent.putExtra("html_center_url", html_center_url);
                        getContext().startActivity(intent);
                    }
                });
            }
            if (!trackable_type.equals("bet")) {
                linearJugar.setVisibility(View.GONE);
            }
            if (!imageAttached.isEmpty()) {
                linearImageAttached.setVisibility(View.VISIBLE);
                com.advante.golazzos.Helpers.Picasso.with(getContext()).load(imageAttached).transform(new CircleTransform()).into(imageViewAttached);
            }
            //linearImageAttached.setVisibility(View.GONE);

            if (!bundle.getString("profile_pic_url", "").contains("facebook.com")) {
                pic_name = bundle.getString("profile_pic_url", "").substring(
                        bundle.getString("profile_pic_url", "").lastIndexOf("/"),
                        bundle.getString("profile_pic_url", "").lastIndexOf("."));
            } else {
                pic_name = "" + idImage;
            }

            com.advante.golazzos.Helpers.Picasso.with(getContext()).load(gnr.getLoggedUser().getProfile_pic_url()).transform(new CircleTransform()).into(imageEquipo1);

            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack();
                }
            });
            buttonFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    publishFacebookFeed();
                }
            });
            buttonTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    publishTwitterFeed();
                }
            });
            buttonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (idLike > 0) {
                        unlike();
                    } else {
                        like();
                    }
                }
            });
            buttonComentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linear1.setVisibility(View.VISIBLE);
                    editComentario.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editComentario, InputMethodManager.SHOW_IMPLICIT);

                }
            });

            buttonPublicar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }
                    Comentar();
                }
            });

            linearJugar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment f = new PartidosPorJugar_Fragment();
                    Bundle b = new Bundle();
                    b.putString("idPartido", "" + idPartido);
                    f.setArguments(b);
                    ft.replace(R.id.flContent, f, "");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            final Resources r = getResources();
            final int dp50 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    50,
                    r.getDisplayMetrics()
            );

            getData();
        }
        return view;
    }

    private void publishTwitterFeed(){
        dialog.show();
        JSONObject request = new JSONObject();
        try {
            request.put("longUrl", "https://play.google.com/store/apps/details?id=com.advante.golazzos&referrer="+gnr.getLoggedUser().getInvitation_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        API.getInstance(getContext()).unAuthenticateObjectRequest(Request.Method.POST, "https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyBqKNa1AIrPJsa8zQTfsUTgumE8TNKlu2k", request, new API_Listener() {
            @Override
            public void OnSuccess(JSONObject response) {
                dialog.dismiss();
                TweetComposer.Builder builderTW = null;
                try {
                    builderTW = new TweetComposer.Builder(getContext())
                            .text(label.replace("<font color='#0E5A80'>","").replace("</font>","").replace("<br>","").replace("<inviter>","").replace("</inviter>","") +" Juega Ya en Golazzos! Descarga la APP. "+ response.getString("id"));
                    builderTW.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnSuccess(JSONArray response) {

            }

            @Override
            public void OnError(VolleyError error) {
                dialog.dismiss();
            }
        });
    }

    private void publishFacebookFeed(){
        callbackManager = CallbackManager.Factory.create();
        final ShareDialog shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(General.appname, "success");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(General.appname, "error "+error.getMessage() );
            }

            @Override
            public void onCancel() {
                Log.d(General.appname, "cancel");
            }
        });

        if (shareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Golazzos")
                    .setContentDescription(label.replace("<font color='#0E5A80'>","").replace("</font>","").replace("<br>","").replace("<inviter>","").replace("</inviter>",""))
                    .setContentUrl(Uri.parse(getString(R.string.baseLanding)+gnr.getLoggedUser().getInvitation_token()))
                    .setImageUrl(Uri.parse(getString(R.string.baseLandingIco)))
                    .build();
            shareDialog.show(linkContent);
        }

    }

    private void like(){
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_posts +"/"+id+"/likes",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data = response.getJSONObject("response");
                            idLike = data.getInt("id");
                            textLike.setText("No me gusta");
                            showShortToast("Me gusta.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        for (String name: error.networkResponse.headers.keySet()){
                            String key =name.toString();
                            String value = error.networkResponse.headers.get(name).toString();
                            showLog(key + " " + value);
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
    private void unlike(){
        showLog(General.endpoint_posts +"/"+id+"/likes/"+idLike);
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                General.endpoint_posts +"/"+id+"/likes/"+idLike,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data = response.getJSONObject("response");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textLike.setText("Me gusta");
                        idLike = -1;
                        showShortToast("No me gusta.");
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

    private void getData(){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_posts +"/"+id+"/comments",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONArray datos = response.getJSONArray("response");
                            ArrayList<Comentario> comentarios = new ArrayList<>();
                            Comentario comentario;
                            User user;
                            for(int i=0; i<datos.length(); i++){
                                comentario = new Comentario();
                                comentario.setId(datos.getJSONObject(i).getInt("id"));
                                comentario.setText(datos.getJSONObject(i).getString("text"));
                                comentario.setTime_ago(datos.getJSONObject(i).getString("created"));

                                user = new User();
                                user.setProfile_pic_url(datos.getJSONObject(i).getJSONObject("user").getString("profile_pic_url"));
                                user.setName(datos.getJSONObject(i).getJSONObject("user").getString("name"));
                                comentario.setUser(user);

                                comentarios.add(comentario);
                                comentario.toString();
                            }
                            List_Comentarios adapter = new List_Comentarios(getContext(),comentarios);
                            listView.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLog(""+error.getMessage());
                        //Toast.makeText(getContext(), "Error en al tratar de conectar con el servicio web. Intente mas tarde", Toast.LENGTH_SHORT).show();
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

    private void Comentar(){
        dialog.show();
        JSONObject post = new JSONObject();
        JSONObject values = new JSONObject();

        try {
            values.put("text", editComentario.getText().toString());
            post.put("comment",values);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        linear1.setVisibility(View.INVISIBLE);
        editComentario.setText("");
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_posts +"/"+id+"/comments",
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data = response.getJSONObject("response");
                            getData();
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
