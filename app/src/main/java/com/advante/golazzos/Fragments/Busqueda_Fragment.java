package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Users;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/10/2016.
 */
public class Busqueda_Fragment extends GeneralFragment{
    JsonObjectRequest jsArrayRequest;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        listView = (ListView) view.findViewById(R.id.listview);


        getData(getArguments().getString("search"));
        return view;
    }

    private void getData(String search){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_users_search +search,
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
                                userBusqueda.setIs_friend(datos.getJSONObject(i).getBoolean("is_friend"));

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
