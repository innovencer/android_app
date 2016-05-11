package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.advante.golazzos.Adapters.List_Partidos;
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
public class PartidosPorJugar_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ListView listView;
    TextView buttonEnVivo,buttonFinalizado;
    private ViewFlipper viewFlipper;
    private float lastX;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partidos_1, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        buttonEnVivo = (TextView) view.findViewById(R.id.buttonEnVivo);
        buttonFinalizado = (TextView) view.findViewById(R.id.buttonFinalizado);
        buttonEnVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContent, new PartidosEnVivo_Fragment(), "");
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
        view.setOnTouchListener(touchListener);
        buscarPartidos(R.layout.item_partido_1);
        return view;
    }

    private void buscarPartidos(final int resourse){
        dialog.show();
            jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    gnr.endpoint_maches,
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
                                    partidos.add(partido);

                                }
                                List_Partidos list_partidos = new List_Partidos(getContext(),partidos,resourse);
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
                        buscarPartidos(R.layout.item_partido_1);
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
                        buscarPartidos(R.layout.item_partido_3);
                    }
                    break;
                }
            }
            return true;
        }
    };

}
