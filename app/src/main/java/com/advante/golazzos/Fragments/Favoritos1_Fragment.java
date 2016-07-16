package com.advante.golazzos.Fragments;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Equipos;
import com.advante.golazzos.Adapters.List_Ligas;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.IBuscarLigas_Listener;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Liga;
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
 * Created by Ruben Flores on 4/30/2016.
 */
public class Favoritos1_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ImageView picView;
    TextView buttonLigas,buttonEquipos, buttonGuardar, textName;
    LinearLayout buttonFavoritos2, linear4;
    int idLiga = -1,idEquipo = -1,idLiga_Temp = -1;
    ArrayList<Liga> ligas;
    ArrayList<Equipo> equipos;
    SoulTeam soulteamTemp;
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
        View view = inflater.inflate(R.layout.fragment_favoritos1, container, false);
        if(flag) {
            picView = (ImageView) view.findViewById(R.id.imageEquipoAlma);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            // picView.setImageBitmap(graphicUtil.getRoundedShape(thePic,(float)1.5,92));
            picView.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));

            buttonLigas = (TextView) view.findViewById(R.id.buttonLiga);
            buttonEquipos = (TextView) view.findViewById(R.id.buttonEquipo);
            buttonGuardar = (TextView) view.findViewById(R.id.buttonGuardar);
            buttonFavoritos2 = (LinearLayout) view.findViewById(R.id.buttonFavoritos2);
            linear4 = (LinearLayout) view.findViewById(R.id.linear4);

            textName = (TextView) view.findViewById(R.id.textName);
            if (gnr.getLoggedUser().getSoul_team() != null) {
                textName.setText(gnr.getLoggedUser().getSoul_team().getName());
                setImage();
            }

            buttonLigas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (equipos != null) {
                        equipos = null;
                        idEquipo = -1;
                        buttonEquipos.setText("Seleccionar Equipo");
                    }
                    gnr.buscarLigas(new IBuscarLigas_Listener() {
                        @Override
                        public void onComplete(ArrayList<Liga> items) {
                            ligas = items;
                            if (ligas != null) {
                                showDialogLigas(ligas);
                            }
                        }
                    });
                }
            });
            buttonEquipos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (idLiga != -1) {
                        dialog.show();
                        buscarEquipos();
                    } else {
                        Toast.makeText(getContext(), "Debe primero seleccionar una liga.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            buttonGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (idEquipo != -1) {
                        postEquipoAlma();
                    } else {
                        Toast.makeText(getContext(), "Debe seleccionar un equipo para continuar.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            buttonFavoritos2.setOnClickListener(clickFavoritos1);
            linear4.setOnClickListener(clickFavoritos1);
        }
        return view;
    }

    View.OnClickListener clickFavoritos1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Favoritos2_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };

    private void setImage(){
        String imagePath = gnr.getLoggedUser().getSoul_team().getImage_path();
        int idImage = Integer.parseInt(imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.lastIndexOf("-")));
        File file = new File(General.local_dir_images + "equipos/" + idImage + ".gif");
        showLog(file.getAbsolutePath());
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            picView.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            picView.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
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
                if (name.length() > 23) {
                    buttonLigas.setText(name.substring(0, 22));
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
                textName.setText(arrayList.get(i).getName());
                String imagePath = arrayList.get(i).getImage_path();
                int idImage = arrayList.get(i).getData_factory_id();//imagePath.substring(imagePath.lastIndexOf("/")+1,imagePath.lastIndexOf("-"));
                File file = new File(General.local_dir_images + "equipos/" + idImage + ".gif");
                showLog(file.getAbsolutePath());
                if (file.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    GraphicsUtil graphicUtil = new GraphicsUtil();
                    picView.setImageBitmap(graphicUtil.getCircleBitmap(
                            bm, 16));
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
                    GraphicsUtil graphicUtil = new GraphicsUtil();
                    picView.setImageBitmap(graphicUtil.getCircleBitmap(
                            bm, 16));
                }
                dialog.dismiss();
            }
        });
    }

    private void postEquipoAlma(){
        dialog.show();
        JSONObject post = new JSONObject();
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("team_id", idEquipo);
            post.put("soul_team",parametros);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                General.endpoint_soul_team,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        dialog.dismiss();
                        try {
                            JSONObject data = response.getJSONObject("response");
                            gnr.getLoggedUser().setSoul_team(soulteamTemp);
                            showShortToast("Se ha guardado el equipo del alma.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = "";
                        //get status code here

                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if(statusCode != null && statusCode.equals("422")){
                            if(error.networkResponse.data!=null) {
                                try {
                                    body = new String(error.networkResponse.data,"UTF-8");
                                    showLog(body);
                                    showShortToast("Este equipo ya esta dentro de sus favoritos.");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            showShortToast("Error en la comunicacion, por favor intente mas tarde.");
                        }
                        dialog.dismiss();

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token "+ gnr.getToken());
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
