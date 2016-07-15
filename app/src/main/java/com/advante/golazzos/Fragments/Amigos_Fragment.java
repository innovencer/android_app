package com.advante.golazzos.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Users;
import com.advante.golazzos.Helpers.API;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.JSONBuilder;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.MainActivity;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.SendButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/10/2016.
 */
public class Amigos_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ListView listView;
    LinearLayout linear1, linear2, linear3, linear4;
    TextView textNoAmigos;
    ImageView imgInvitar, imgNoAmigos;
    int normalHeight = 0;
    private CallbackManager callbackManager;
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
        final View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        if(flag) {
            FacebookSdk.sdkInitialize(getContext());
            listView = (ListView) view.findViewById(R.id.listview);
            linear1 = (LinearLayout) view.findViewById(R.id.linear1);
            linear2 = (LinearLayout) view.findViewById(R.id.linear2);
            linear3 = (LinearLayout) view.findViewById(R.id.linear3);
            linear4 = (LinearLayout) view.findViewById(R.id.linear4);

            imgInvitar = (ImageView) view.findViewById(R.id.imgInvitar);
            imgNoAmigos = (ImageView) view.findViewById(R.id.imgNoAmigos);

            textNoAmigos = (TextView) view.findViewById(R.id.textNoAmigos);

            linear1.setOnClickListener(clickTab);
            linear2.setOnClickListener(clickTab);
            linear3.setOnClickListener(clickTab);
            linear4.setOnClickListener(clickTab);

            final String appLinkUrl;

            appLinkUrl = "https://play.google.com/store/apps/details?id=com.advante.golazzos&referrer=utm_source%3Dfacebook%26utm_medium%3Dmessenger%26utm_content%3Did%253A" + gnr.getLoggedUser().getId();

            final AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .build();

            imgInvitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AppInviteDialog.canShow()) {
                        AppInviteDialog.show(getActivity(), content);
                    }
                }
            });

            callbackManager = CallbackManager.Factory.create();
            SendButton sendButton = (SendButton) view.findViewById(R.id.button_facebook);
            sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    showLog(result.toString());
                }

                @Override
                public void onCancel() {
                    showLog("cancelled");
                }

                @Override
                public void onError(FacebookException error) {
                    showLog(error.getMessage());
                }
            });

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Juega Conmigo Golazzos!")
                    .setContentDescription("ATREVETE a competir conmigo en GOLAZZOS, el primer JUEGO SOCIAL de predicciones de FUTBOL! Descarga la APP.")
                    .setImageUrl(Uri.parse("http://principal-desarrollo.com/golazzos/img/ic_main.png"))
                    .setContentUrl(Uri.parse(appLinkUrl))
                    .build();

            sendButton.setShareContent(linkContent);

            getData(General.endpoint_friends);

            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (normalHeight == 0) {
                        normalHeight = view.getHeight();
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , LinearLayout.LayoutParams.WRAP_CONTENT);

                    if (view.getHeight() < normalHeight) {
                        imgNoAmigos.setVisibility(View.GONE);
                        textNoAmigos.setVisibility(View.GONE);
                    } else if (view.getHeight() == normalHeight) {
                        checkItems();
                    }

                }
            });
        }
        return view;
    }

    private void checkItems(){
        if(listView.getAdapter()!= null && listView.getAdapter().getCount() > 0){
            imgNoAmigos.setVisibility(View.GONE);
            textNoAmigos.setVisibility(View.GONE);
        }else{
            imgNoAmigos.setVisibility(View.VISIBLE);
            textNoAmigos.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener clickTab = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().equals("1")){
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.INVISIBLE);
                getData(General.endpoint_friends);
            }else{
                linear3.setVisibility(View.INVISIBLE);
                linear4.setVisibility(View.VISIBLE);
                getData(General.endpoint_followers);
            }
        }
    };

    private void getData(String endpoint){
        dialog.show();
        if(endpoint.equals(gnr.endpoint_friends)){
            textNoAmigos.setText(getString(R.string.noAmigos));
        }else if (endpoint.equals(gnr.endpoint_followers)){
            textNoAmigos.setText(getString(R.string.noSeguidores));
        }
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                endpoint,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONArray datos = response.getJSONArray("response");
                            ArrayList<UserBusqueda> userBusquedas = new ArrayList<>();
                            UserBusqueda userBusqueda;
                            SoulTeam soulTeam;
                            UserLevel userLevel;
                            for(int i=0; i<datos.length(); i++){
                                userBusqueda = new UserBusqueda();
                                userBusqueda.setId(datos.getJSONObject(i).getInt("id"));
                                userBusqueda.setName(datos.getJSONObject(i).getString("name"));
                                userBusqueda.setEmail(datos.getJSONObject(i).getString("email"));
                                userBusqueda.setPaid_subscription(datos.getJSONObject(i).getBoolean("paid_subscription"));
                                userBusqueda.setPoints(datos.getJSONObject(i).getDouble("points"));
                                userBusqueda.setProfile_pic_url(datos.getJSONObject(i).getString("profile_pic_url"));
                                if(datos.getJSONObject(i).has("is_friend"))
                                    userBusqueda.setIs_friend(datos.getJSONObject(i).getBoolean("is_friend"));
                                else
                                    userBusqueda.setIs_friend(false);

                                if(datos.getJSONObject(i).has("soul_team")){
                                    soulTeam = new SoulTeam(datos.getJSONObject(i).getJSONObject("soul_team").getString("image_path"),
                                            datos.getJSONObject(i).getJSONObject("soul_team").getString("name"),
                                            datos.getJSONObject(i).getJSONObject("soul_team").getInt("id"));
                                }else{
                                    soulTeam = new SoulTeam(null,
                                            null,
                                            -1);
                                }
                                userBusqueda.setSoul_team(soulTeam);

                                if(datos.getJSONObject(i).has("level")){
                                    userLevel = new UserLevel(datos.getJSONObject(i).getJSONObject("level").getInt("hits_count"),
                                            datos.getJSONObject(i).getJSONObject("level").getString("logo_url"),
                                            datos.getJSONObject(i).getJSONObject("level").getString("name"),
                                            datos.getJSONObject(i).getJSONObject("level").getInt("order"),
                                            datos.getJSONObject(i).getJSONObject("level").getInt("points"));
                                    userBusqueda.setLevel(userLevel);
                                }
                                if(i%2==0){
                                    userBusqueda.setType(1);
                                }else{
                                    userBusqueda.setType(2);
                                }
                                userBusquedas.add(userBusqueda);
                                showLog(userBusqueda.toString());
                            }
                            List_Users adapter = new List_Users(getContext(),userBusquedas);
                            listView.setAdapter(adapter);
                            if(userBusquedas.size()>0) {
                                listView.setVisibility(View.VISIBLE);
                                imgNoAmigos.setVisibility(View.GONE);
                            }else{
                                listView.setVisibility(View.GONE);
                                imgNoAmigos.setVisibility(View.VISIBLE);
                            }
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
                        Toast.makeText(getContext(), "Error en al tratar de conectar con el servicio web. Intente mas tarde", Toast.LENGTH_SHORT).show();
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
}
