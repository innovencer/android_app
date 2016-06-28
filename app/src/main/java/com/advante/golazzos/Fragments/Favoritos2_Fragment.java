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
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Liga;
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
public class Favoritos2_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ImageView imageEquipo1,imageEquipo2,imageEquipo3,imageEquipo4,imageEquipo5,imageEquipo6,imageEquipo7,imageEquipo8;
    ImageView imageElimina1,imageElimina2,imageElimina3,imageElimina4,imageElimina5,imageElimina6,imageElimina7,imageElimina8;
    TextView buttonLigas,buttonEquipos,buttonSiguiente,buttonAgregar;
    TextView textEquipo1,textEquipo2,textEquipo3,textEquipo4,textEquipo5,textEquipo6,textEquipo7,textEquipo8;
    LinearLayout buttonFavoritos1,linear3;
    int idLiga = -1,idEquipo = -1,idLiga_Temp = -1,idEquipoDataF = -1;
    ArrayList<Liga> ligas;
    ArrayList<Equipo> equipos;
    String nameTemp = "";
    int equipos_s[] = new int[]{-1,-1,-1,-1,-1,-1,-1,-1};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos2, container, false);

        buttonLigas = (TextView) view.findViewById(R.id.buttonLiga);
        buttonEquipos = (TextView) view.findViewById(R.id.buttonEquipo);
        buttonAgregar = (TextView) view.findViewById(R.id.buttonAgregar);
        buttonSiguiente = (TextView) view.findViewById(R.id.buttonSiguiente);
        buttonFavoritos1 = (LinearLayout) view.findViewById(R.id.buttonFavoritos1);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);

        imageElimina1 = (ImageView) view.findViewById(R.id.imageElimina1);
        imageElimina2 = (ImageView) view.findViewById(R.id.imageElimina2);
        imageElimina3 = (ImageView) view.findViewById(R.id.imageElimina3);
        imageElimina4 = (ImageView) view.findViewById(R.id.imageElimina4);
        imageElimina5 = (ImageView) view.findViewById(R.id.imageElimina5);
        imageElimina6 = (ImageView) view.findViewById(R.id.imageElimina6);
        imageElimina7 = (ImageView) view.findViewById(R.id.imageElimina7);
        imageElimina8 = (ImageView) view.findViewById(R.id.imageElimina8);

        imageElimina1.setOnClickListener(onEliminarClick);
        imageElimina2.setOnClickListener(onEliminarClick);
        imageElimina3.setOnClickListener(onEliminarClick);
        imageElimina4.setOnClickListener(onEliminarClick);
        imageElimina5.setOnClickListener(onEliminarClick);
        imageElimina6.setOnClickListener(onEliminarClick);
        imageElimina7.setOnClickListener(onEliminarClick);
        imageElimina8.setOnClickListener(onEliminarClick);

        imageEquipo1 = (ImageView) view.findViewById(R.id.imageEquipo1);
        imageEquipo2 = (ImageView) view.findViewById(R.id.imageEquipo2);
        imageEquipo3 = (ImageView) view.findViewById(R.id.imageEquipo3);
        imageEquipo4 = (ImageView) view.findViewById(R.id.imageEquipo4);
        imageEquipo5 = (ImageView) view.findViewById(R.id.imageEquipo5);
        imageEquipo6 = (ImageView) view.findViewById(R.id.imageEquipo6);
        imageEquipo7 = (ImageView) view.findViewById(R.id.imageEquipo7);
        imageEquipo8 = (ImageView) view.findViewById(R.id.imageEquipo8);

        textEquipo1 = (TextView) view.findViewById(R.id.textEquipo1);
        textEquipo2 = (TextView) view.findViewById(R.id.textEquipo2);
        textEquipo3 = (TextView) view.findViewById(R.id.textEquipo3);
        textEquipo4 = (TextView) view.findViewById(R.id.textEquipo4);
        textEquipo5 = (TextView) view.findViewById(R.id.textEquipo5);
        textEquipo6 = (TextView) view.findViewById(R.id.textEquipo6);
        textEquipo7 = (TextView) view.findViewById(R.id.textEquipo7);
        textEquipo8 = (TextView) view.findViewById(R.id.textEquipo8);

        for(int i = 1; i <= 8; i++){
            clearImage(i);
        }

        buttonLigas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equipos != null){
                    equipos = null;
                    idEquipo = -1;
                    buttonEquipos.setText("Equipo");
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
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray ids = new JSONArray();
                for(int i=0;i<=7;i++){
                    if(equipos_s[i] != -1){
                        ids.put(equipos_s[i]);
                    }
                }
                if(ids.length()>0){
                    JSONObject post = new JSONObject();
                    try {
                        post.put("favorite_team_ids",ids);
                        showLog(post.toString());
                        postFavoritos(post);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!agregado(idEquipo)) {
                    for (int i = 0; i < 8; i++) {
                        if (equipos_s[i] == -1) {
                            equipos_s[i] = idEquipo;
                            setImage(i + 1, idEquipoDataF, nameTemp);
                            return;
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"Ya selecciono este equipo.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonFavoritos1.setOnClickListener(clickFavoritos1);
        linear3.setOnClickListener(clickFavoritos1);

        buscaEquipos();
        return view;
    }

    View.OnClickListener clickFavoritos1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Favoritos1_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };

    private void buscaEquipos(){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_favorites,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("response");
                            equipos = new ArrayList<>();
                            Equipo equipo;
                            int count=0;
                            for (int i = 0; i < data.length(); i++) {
                                if(!data.getJSONObject(i).getBoolean("soul")) {
                                    equipo = new Equipo(data.getJSONObject(i).getJSONObject("team").getInt("id"),
                                            data.getJSONObject(i).getJSONObject("team").getString("name"),
                                            data.getJSONObject(i).getJSONObject("team").getString("complete_name"),
                                            data.getJSONObject(i).getJSONObject("team").getString("country_name"),
                                            data.getJSONObject(i).getJSONObject("team").getString("image_path"),
                                            "",
                                            data.getJSONObject(i).getJSONObject("team").getString("initials"),
                                            data.getJSONObject(i).getJSONObject("team").getInt("data_factory_id"));
                                    setImage(count + 1, equipo.getData_factory_id(),equipo.getName());
                                    equipos.add(equipo);
                                    equipos_s[count] = equipo.getId();
                                    count++;
                                }
                            }
                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Error al conectar al servicio",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        showShortToast("Error al conectar al servicio");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token "+ General.getToken());
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

    View.OnClickListener onEliminarClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageElimina1:
                    clearImage(1);
                    break;
                case R.id.imageElimina2:
                    clearImage(2);
                    break;
                case R.id.imageElimina3:
                    clearImage(3);
                    break;
                case R.id.imageElimina4:
                    clearImage(4);
                    break;
                case R.id.imageElimina5:
                    clearImage(5);
                    break;
                case R.id.imageElimina6:
                    clearImage(6);
                    break;
                case R.id.imageElimina7:
                    clearImage(7);
                    break;
                case R.id.imageElimina8:
                    clearImage(8);
                    break;
            }
        }
    };

    private Boolean agregado(int id){
        for(int i = 0;i<=7;i++){
            if(equipos_s[i]==id){
                return true;
            }
        }
        return false;
    }

    private void clearImage(int num_image){
        ImageView imageView = null,imageView1 = null;
        TextView textView;
        switch (num_image){
            case 1:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
                textView = textEquipo1;
                break;
            case 2:
                imageView = imageEquipo2;
                imageView1 = imageElimina2;
                textView = textEquipo2;
                break;
            case 3:
                imageView = imageEquipo3;
                imageView1 = imageElimina3;
                textView = textEquipo3;
                break;
            case 4:
                imageView = imageEquipo4;
                imageView1 = imageElimina4;
                textView = textEquipo4;
                break;
            case 5:
                imageView = imageEquipo5;
                imageView1 = imageElimina5;
                textView = textEquipo5;
                break;
            case 6:
                imageView = imageEquipo6;
                imageView1 = imageElimina6;
                textView = textEquipo6;
                break;
            case 7:
                imageView = imageEquipo7;
                imageView1 = imageElimina7;
                textView = textEquipo7;
                break;
            case 8:
                imageView = imageEquipo8;
                imageView1 = imageElimina8;
                textView = textEquipo8;
                break;
            default:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
                textView = textEquipo1;
        }
        equipos_s[num_image-1] = -1;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
        GraphicsUtil graphicUtil = new GraphicsUtil();
        // picView.setImageBitmap(graphicUtil.getRoundedShape(thePic,(float)1.5,92));
        imageView.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));
        imageView1.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    private void setImage(int num_image, int idDataFactory, String name){
        ImageView imageView = null,imageView1 = null;
        TextView textView;
        switch (num_image){
            case 1:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
                textView = textEquipo1;
                break;
            case 2:
                imageView = imageEquipo2;
                imageView1 = imageElimina2;
                textView = textEquipo2;
                break;
            case 3:
                imageView = imageEquipo3;
                imageView1 = imageElimina3;
                textView = textEquipo3;
                break;
            case 4:
                imageView = imageEquipo4;
                imageView1 = imageElimina4;
                textView = textEquipo4;
                break;
            case 5:
                imageView = imageEquipo5;
                imageView1 = imageElimina5;
                textView = textEquipo5;
                break;
            case 6:
                imageView = imageEquipo6;
                imageView1 = imageElimina6;
                textView = textEquipo6;
                break;
            case 7:
                imageView = imageEquipo7;
                imageView1 = imageElimina7;
                textView = textEquipo7;
                break;
            case 8:
                imageView = imageEquipo8;
                imageView1 = imageElimina8;
                textView = textEquipo8;
                break;
            default:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
                textView = textEquipo1;
        }


        int idImage = idDataFactory;
        textView.setText(name);
        if(name.length()>10){
            textView.setTextSize(10);
        }
        textView.setVisibility(View.VISIBLE);
        File file = new File(General.local_dir_images +"equipos/"+idImage+".gif");
        if(file.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageView.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
            imageView1.setVisibility(View.VISIBLE);
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageView.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
            imageView1.setVisibility(View.VISIBLE);
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
            showDialogEquipos(equipos);
            dialog.dismiss();
        }
    }

    private void showDialogLigas(final ArrayList<Liga> arrayList){
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);

        List_Ligas arrayAdapter = new List_Ligas(getContext(), arrayList);
        final ListView listView = (ListView) dialog.findViewById(R.id.listview);
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
                nameTemp = arrayList.get(i).getName();
                idEquipoDataF = arrayList.get(i).getData_factory_id();
                //buttonEquipos.setText(arrayList.get(i).getName());
                if (!agregado(idEquipo)) {
                    for (int j = 0; j < 8; j++) {
                        if (equipos_s[j] == -1) {
                            equipos_s[j] = idEquipo;
                            setImage(j + 1, idEquipoDataF, nameTemp);
                            if(j == 7){
                                buttonEquipos.setText(nameTemp);
                            }
                            dialog.dismiss();
                            return;
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"Ya selecciono este equipo.",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void postFavoritos(JSONObject post){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                General.endpoint_favorites +"/batch_update",
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONArray data = response.getJSONArray("response");
                            dialog.dismiss();
                            showShortToast("Se han guardado sus equipos favoritos.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showShortToast("Error en la comunicacion, por favor intente mas tarde.");
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
