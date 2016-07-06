package com.advante.golazzos.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Ganadores;
import com.advante.golazzos.Adapters.List_Semanas;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.Ganador;
import com.advante.golazzos.Model.Ganador_Item;
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
 * Created by Ruben Flores on 5/25/2016.
 */
public class Fragment_Ganadores extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ArrayList<Ganador> ganadores;
    ListView listView;
    TextView textSemana;
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
        View view = inflater.inflate(R.layout.fragment_ganadores, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        textSemana = (TextView) view.findViewById(R.id.textSemana);
        textSemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSemanas();
            }
        });
        if(flag) {
            getData(gnr.endpoint_weekly_awards);
        }
        return view;
    }

    private void getData(String endpoint){
        dialog.show();
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
                            Ganador ganador;
                            ganadores = new ArrayList<>();
                            ArrayList<Ganador_Item> ganador_items;
                            Ganador_Item acierto_item;
                            for(int i=0; i<datos.length(); i++){
                                ganador_items = new ArrayList<>();
                                for(int j=0;j<datos.getJSONObject(i).getJSONArray("ranking_entries").length();j++){
                                    acierto_item = new Ganador_Item();
                                    acierto_item.setRank(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getInt("rank"));
                                    acierto_item.setPremios(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getInt("prize"));
                                    acierto_item.setScore(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getInt("score"));
                                    acierto_item.setIdProfile(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getJSONObject("winner").getInt("id"));
                                    acierto_item.setName(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getJSONObject("winner").getString("name"));
                                    acierto_item.setPatchProfileImage(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getJSONObject("winner").getString("profile_pic_url"));
                                    if(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getJSONObject("winner").has("soul_team")) {
                                        acierto_item.setPatchSoulTeam(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getJSONObject("winner").getJSONObject("soul_team").getString("image_path"));
                                        acierto_item.setSouldTeamName(datos.getJSONObject(i).getJSONArray("ranking_entries").getJSONObject(j).getJSONObject("winner").getJSONObject("soul_team").getString("name"));
                                    }
                                    if(j<5){
                                        acierto_item.setType(j+2);
                                    }
                                    ganador_items.add(acierto_item);
                                }
                                ganador = new Ganador(datos.getJSONObject(i).getString("week_label"), ganador_items);
                                ganadores.add(ganador);
                            }
                            //listView.setAdapter(adapter);
                            dialog.dismiss();
                            showSemana(0);
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

    private void showSemana(int semana){
        if(ganadores.size()>0){
            Ganador acierto = ganadores.get(semana);
            textSemana.setText(acierto.getWeek_label());
            List_Ganadores adapter = new List_Ganadores(getContext(), acierto.getRanking_entries());
            listView.setAdapter(adapter);
        }
    }

    private void showDialogSemanas(){
        final Dialog dialog = new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input);

        List_Semanas adapter = new List_Semanas(getContext(), ganadores);
        ListView listView = (ListView) dialog.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showSemana(i);
                dialog.dismiss();
            }
        });
        listView.setAdapter(adapter);
        dialog.show();
    }
}
