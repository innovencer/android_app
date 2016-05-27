package com.advante.golazzos.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.advante.golazzos.Adapters.List_Equipos;
import com.advante.golazzos.Adapters.List_Jugadas;
import com.advante.golazzos.Adapters.List_Ligas;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Jugada;
import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.R;
import com.advante.golazzos.widget.DividerItemDecoration;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/27/2016.
 */
public class Jugadas_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    RecyclerView recycler;
    TextView buttonFinalizado, buttonEnVivo, buttonPORJUGAR;

    private ViewFlipper viewFlipper;
    private float lastX;

    TextView buttonLigas,buttonEquipos;
    String equipo = "";
    int idLiga = -1,idEquipo = -1,idLiga_Temp = -1;
    ArrayList<Liga> ligas;
    ArrayList<Equipo> equipos;

    String status = "?status=not_started",bet_type_id = "";
    int tab = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jugadas_realizadas_1, container, false);

        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        buttonFinalizado = (TextView) view.findViewById(R.id.buttonFinalizado);
        buttonEnVivo = (TextView) view.findViewById(R.id.buttonEnVivo);
        buttonPORJUGAR = (TextView) view.findViewById(R.id.buttonPORJUGAR);
        buttonLigas = (TextView) view.findViewById(R.id.buttonLiga);
        buttonEquipos = (TextView) view.findViewById(R.id.buttonEquipo);

        buttonFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuTint(3);
                status = "?status=finished";
                getData(status+bet_type_id);
            }
        });
        buttonPORJUGAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuTint(1);
                status = "?status=not_started";
                getData(status+bet_type_id);
            }
        });
        buttonEnVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuTint(2);
                status = "?status=being_played";
                getData(status+bet_type_id);
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

        view.setOnTouchListener(touchListener);
        getData(status+bet_type_id);

        return view;
    }

    private void getData(String param){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_bets+param,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONArray data = response.getJSONArray("response");
                            ArrayList<Jugada> jugadas = new ArrayList<>();
                            Jugada jugada;
                            Equipo equipo;
                            for(int i = 0;i < data.length(); i++){
                                jugada = new Jugada();
                                jugada.setId(data.getJSONObject(i).getInt("id"));
                                jugada.setTextTime_ago("Waiting");

                                equipo = new Equipo();
                                equipo.setData_factory_id(data.getJSONObject(i).getJSONObject("match").getJSONObject("local_team").getInt("data_factory_id"));
                                equipo.setName(data.getJSONObject(i).getJSONObject("match").getJSONObject("local_team").getString("name"));
                                equipo.setInitials(data.getJSONObject(i).getJSONObject("match").getJSONObject("local_team").getString("initials"));
                                jugada.setEquipo1(equipo);

                                equipo = new Equipo();
                                equipo.setData_factory_id(data.getJSONObject(i).getJSONObject("match").getJSONObject("visitant_team").getInt("data_factory_id"));
                                equipo.setName(data.getJSONObject(i).getJSONObject("match").getJSONObject("visitant_team").getString("name"));
                                equipo.setInitials(data.getJSONObject(i).getJSONObject("match").getJSONObject("visitant_team").getString("initials"));
                                jugada.setEquipo2(equipo);

                                if(i%2 == 0){
                                    jugada.setType(2);
                                }else{
                                    jugada.setType(1);
                                }
                                jugadas.add(jugada);
                            }

                            List_Jugadas adapter = new List_Jugadas(getActivity(), jugadas);
                            recycler.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
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

    private void menuTint(int option){
        buttonLigas.setText("Ligas");
        buttonEquipos.setText("Equipos");
        switch (option){
            case 1:
                buttonPORJUGAR.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
                buttonEnVivo.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.liteGray));
                buttonFinalizado.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.liteGray));

                buttonPORJUGAR.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                buttonEnVivo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                buttonFinalizado.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                tab = 1;
                break;
            case 2:
                buttonPORJUGAR.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.liteGray));
                buttonEnVivo.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenApple));
                buttonFinalizado.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.liteGray));

                buttonPORJUGAR.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                buttonEnVivo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                buttonFinalizado.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                tab = 2;
                break;
            case 3:
                buttonPORJUGAR.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.liteGray));
                buttonEnVivo.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.liteGray));
                buttonFinalizado.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));

                buttonPORJUGAR.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                buttonEnVivo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                buttonFinalizado.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tab = 3;
                break;
        }
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
                idEquipo = -1;
                equipo = "";
                String name = arrayList.get(i).getName();
                if (name.length() > 18) {
                    buttonLigas.setText(name.substring(0, 18));
                } else {
                    buttonLigas.setText(name);
                }
                getData(status+bet_type_id+"&tournament_id="+idLiga);
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
                equipo = arrayList.get(i).getName();
                buttonEquipos.setText(arrayList.get(i).getName());
                getData(status+bet_type_id+"&team_id="+idEquipo);
                dialog.dismiss();

            }
        });
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent touchevent) {
            switch (touchevent.getAction())
            {
                // when user first touches the screen to swap
                case MotionEvent.ACTION_DOWN:
                {
                    lastX = touchevent.getX();
                    break;
                }
                case MotionEvent.ACTION_UP:
                {
                    float currentX = touchevent.getX();

                    // if left to right swipe on screen
                    if (lastX < currentX)
                    {
                        // If no more View/Child to flip
                        if (viewFlipper.getDisplayedChild() == 0)
                            break;

                        // set the required Animation type to ViewFlipper
                        // The Next screen will come in form Left and current Screen will go OUT from Right
                        viewFlipper.setInAnimation(getContext(), R.anim.in_from_left);
                        viewFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
                        // Show the next Screen
                        viewFlipper.showNext();
                        bet_type_id = "";
                        getData(status+bet_type_id);
                    }

                    // if right to left swipe on screen
                    if (lastX > currentX)
                    {
                        if (viewFlipper.getDisplayedChild() == 1)
                            break;
                        // set the required Animation type to ViewFlipper
                        // The Next screen will come in form Right and current Screen will go OUT from Left
                        viewFlipper.setInAnimation(getContext(), R.anim.in_from_right);
                        viewFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                        // Show The Previous Screen
                        viewFlipper.showPrevious();
                        bet_type_id = "&bet_type_id=1";
                        getData(status+bet_type_id);
                    }
                    break;
                }
            }
            return true;
        }
    };
}
