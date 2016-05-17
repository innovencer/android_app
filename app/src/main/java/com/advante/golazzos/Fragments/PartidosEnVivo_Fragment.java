package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Partidos;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.Model.Partido;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 4/22/2016.
 */
public class PartidosEnVivo_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ListView listView;
    TextView buttonPorJugar, buttonFinalizado;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partidos_2, container, false);
        listView = (ListView) view.findViewById(R.id.listview);

        buttonPorJugar = (TextView) view.findViewById(R.id.buttonPorJugar);
        buttonFinalizado = (TextView) view.findViewById(R.id.buttonFinalizado);

        buttonPorJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContent, new PartidosPorJugar_Fragment(), "");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        buttonFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContent, new PartidosFinalizado_Fragment(), "");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        buscarPartidos();
        return view;
    }


    private void buscarPartidos(){
        dialog.show();
            jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    General.endpoint_maches_playing,
                    "",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Manejo de la respuesta
                            try {
                                JSONArray data = response.getJSONArray("response");
                                ArrayList<Partido> partidos = new ArrayList<>();
                                Partido partido;
                                for(int i=0;i< data.length();i++){
                                    partido = new Partido();
                                    partido.setId(data.getJSONObject(i).getInt("id"));
                                    partido.setStart_time_utc(data.getJSONObject(i).getString("start_time_utc"));
                                    partido.setHtml_center_url(data.getJSONObject(i).getString("html_center_url"));
                                    partido.setTournament(new Liga(0,
                                            data.getJSONObject(i).getJSONObject("tournament").getInt("id"),
                                            data.getJSONObject(i).getJSONObject("tournament").getString("name")));
                                    partido.setLocal(new Equipo(data.getJSONObject(i).getJSONObject("local_team").getInt("id"),
                                            data.getJSONObject(i).getJSONObject("local_team").getString("name"),
                                            data.getJSONObject(i).getJSONObject("local_team").getString("image_path")));
                                    partido.setVisitante(new Equipo(data.getJSONObject(i).getJSONObject("visitant_team").getInt("id"),
                                            data.getJSONObject(i).getJSONObject("visitant_team").getString("name"),
                                            data.getJSONObject(i).getJSONObject("visitant_team").getString("image_path")));
                                    partido.setLocal_score(data.getJSONObject(i).getInt("local_score"));
                                    partido.setVisitant_score(data.getJSONObject(i).getInt("visitant_score"));
                                    partidos.add(partido);

                                }
                                List_Partidos list_partidos = new List_Partidos(getContext(),partidos,R.layout.item_partido_2);
                                listView.setAdapter(list_partidos);
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
