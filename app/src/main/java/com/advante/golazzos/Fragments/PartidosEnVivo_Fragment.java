package com.advante.golazzos.Fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Equipos;
import com.advante.golazzos.Adapters.List_Ligas;
import com.advante.golazzos.Adapters.List_Partidos;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.Model.Partido;
import com.advante.golazzos.Model.SoulTeam;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
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

    TextView buttonLigas,buttonEquipos;
    int idLiga = -1,idEquipo = -1,idLiga_Temp = -1;
    ArrayList<Liga> ligas;
    ArrayList<Equipo> equipos;
    SoulTeam soulteamTemp;

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
        buttonLigas = (TextView) view.findViewById(R.id.buttonLiga);
        buttonEquipos = (TextView) view.findViewById(R.id.buttonEquipo);

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
        buttonLigas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equipos != null){
                    equipos = null;
                    idEquipo = -1;
                    buttonEquipos.setText("Seleccionar Equipo");
                }
                dialog.show();
                buscarLigas();
            }
        });
        buttonEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idLiga != -1) {
                    dialog.show();
                    buscarEquipos();
                }else{
                    Toast.makeText(getContext(), "Debe primero seleccionar una liga.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscarPartidos();
        return view;
    }

    private void buscarLigas(){
        if(ligas == null) {
            jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    General.endpoint_tournaments,
                    "",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Manejo de la respuesta
                            try {
                                JSONArray data = response.getJSONArray("response");
                                ligas = new ArrayList<>();
                                Liga liga;
                                for (int i = 0; i < data.length(); i++) {
                                    liga = new Liga(data.getJSONObject(i).getInt("data_factory_id"),
                                            data.getJSONObject(i).getInt("id"),
                                            data.getJSONObject(i).getString("name"));
                                    ligas.add(liga);
                                }
                                showDialogLigas(ligas);
                                dialog.dismiss();
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
                            if (error.networkResponse.data != null) {
                                try {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                        }
                    });
            jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    7000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
        }else{
            dialog.dismiss();
            showDialogLigas(ligas);
        }
    }

    private void buscarEquipos(){
        if(idLiga_Temp != idLiga){
            idLiga_Temp = idLiga;
            jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    General.endpoint_teams +"&tournament_id="+idLiga,
                    "",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray data = response.getJSONArray("response");
                                equipos = new ArrayList<>();
                                Equipo equipo;
                                for (int i = 0; i < data.length(); i++) {
                                    equipo = new Equipo(data.getJSONObject(i).getInt("id"),
                                            data.getJSONObject(i).getString("name"),
                                            data.getJSONObject(i).getString("complete_name"),
                                            data.getJSONObject(i).getString("country_name"),
                                            data.getJSONObject(i).getString("image_path"),
                                            "",
                                            data.getJSONObject(i).getString("initials"),
                                            data.getJSONObject(i).getInt("data_factory_id"));
                                    equipos.add(equipo);
                                }
                                showDialogEquipos(equipos);
                                dialog.dismiss();
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
                            if (error.networkResponse.data != null) {
                                try {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                        }
                    });
            jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    7000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
        }else{
            showLog("Pasando temp");
            showDialogEquipos(equipos);
            dialog.dismiss();
        }
    }

    private void showDialogLigas(final ArrayList<Liga> arrayList){
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);

        List_Ligas arrayAdapter = new List_Ligas(getContext(), arrayList);
        final ListView listView = (ListView) dialog.findViewById(R.id.listview);
        TextView textTitulo = (TextView) dialog.findViewById(R.id.textTitulo);
        TextView textSubTitulo = (TextView) dialog.findViewById(R.id.textSubTitulo);
        textTitulo.setText("ESCOGE EL TORNEO");
        textSubTitulo.setVisibility(View.GONE);
        listView.setAdapter(arrayAdapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idLiga = arrayList.get(i).getId();
                String name = arrayList.get(i).getName();
                if (name.length() > 18) {
                    buttonLigas.setText(name.substring(0, 18));
                } else {
                    buttonLigas.setText(name);
                }

                dialog.dismiss();
            }
        });
    }

    private void showDialogEquipos(final ArrayList<Equipo> arrayList){
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);
        TextView textTitulo = (TextView) dialog.findViewById(R.id.textTitulo);
        TextView textSubTitulo = (TextView) dialog.findViewById(R.id.textSubTitulo);
        textTitulo.setText("ESCOGE EL EQUIPO");
        textSubTitulo.setText(buttonLigas.getText());

        List_Equipos arrayAdapter = new List_Equipos(getContext(), arrayList);
        final ListView listView = (ListView) dialog.findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idEquipo = arrayList.get(i).getId();
                soulteamTemp = new SoulTeam(arrayList.get(i).getImage_path(),arrayList.get(i).getName(),arrayList.get(i).getId());
                buttonEquipos.setText(arrayList.get(i).getName());
                dialog.dismiss();
            }
        });
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
