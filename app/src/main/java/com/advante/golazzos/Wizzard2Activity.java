package com.advante.golazzos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Liga;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Ruben Flores on 4/11/2016.
 */
public class Wizzard2Activity extends GeneralActivity {
    JsonObjectRequest jsArrayRequest;
    ImageView imageEquipo1,imageEquipo2,imageEquipo3,imageEquipo4,imageEquipo5,imageEquipo6,imageEquipo7,imageEquipo8;
    ImageView imageElimina1,imageElimina2,imageElimina3,imageElimina4,imageElimina5,imageElimina6,imageElimina7,imageElimina8;
    TextView buttonLigas,buttonEquipos,buttonSiguiente,buttonAgregar;

    int idLiga = -1,idEquipo = -1,idLiga_Temp = -1,idEquipoDataF = -1;
    List<Liga> ligas;
    List<Equipo> equipos;

    int equipos_s[] = new int[]{-1,-1,-1,-1,-1,-1,-1,-1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizzard2);

        buttonLigas = (TextView) findViewById(R.id.buttonLiga);
        buttonEquipos = (TextView) findViewById(R.id.buttonEquipo);
        buttonAgregar = (TextView) findViewById(R.id.buttonAgregar);
        buttonSiguiente = (TextView) findViewById(R.id.buttonSiguiente);

        imageElimina1 = (ImageView) findViewById(R.id.imageElimina1);
        imageElimina2 = (ImageView) findViewById(R.id.imageElimina2);
        imageElimina3 = (ImageView) findViewById(R.id.imageElimina3);
        imageElimina4 = (ImageView) findViewById(R.id.imageElimina4);
        imageElimina5 = (ImageView) findViewById(R.id.imageElimina5);
        imageElimina6 = (ImageView) findViewById(R.id.imageElimina6);
        imageElimina7 = (ImageView) findViewById(R.id.imageElimina7);
        imageElimina8 = (ImageView) findViewById(R.id.imageElimina8);

        imageElimina1.setOnClickListener(onEliminarClick);
        imageElimina2.setOnClickListener(onEliminarClick);
        imageElimina3.setOnClickListener(onEliminarClick);
        imageElimina4.setOnClickListener(onEliminarClick);
        imageElimina5.setOnClickListener(onEliminarClick);
        imageElimina6.setOnClickListener(onEliminarClick);
        imageElimina7.setOnClickListener(onEliminarClick);
        imageElimina8.setOnClickListener(onEliminarClick);

        imageEquipo1 = (ImageView) findViewById(R.id.imageEquipo1);
        imageEquipo2 = (ImageView) findViewById(R.id.imageEquipo2);
        imageEquipo3 = (ImageView) findViewById(R.id.imageEquipo3);
        imageEquipo4 = (ImageView) findViewById(R.id.imageEquipo4);
        imageEquipo5 = (ImageView) findViewById(R.id.imageEquipo5);
        imageEquipo6 = (ImageView) findViewById(R.id.imageEquipo6);
        imageEquipo7 = (ImageView) findViewById(R.id.imageEquipo7);
        imageEquipo8 = (ImageView) findViewById(R.id.imageEquipo8);

        for(int i = 1; i <= 8; i++){
            clearImage(i);
        }

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
                    Toast.makeText(Wizzard2Activity.this, "Debe primero seleccionar una liga.", Toast.LENGTH_SHORT).show();
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
                            setImage(i + 1);
                            return;
                        }
                    }
                }else{
                    Toast.makeText(Wizzard2Activity.this,"Ya selecciono este equipo.",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        switch (num_image){
            case 1:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
                break;
            case 2:
                imageView = imageEquipo2;
                imageView1 = imageElimina2;
                break;
            case 3:
                imageView = imageEquipo3;
                imageView1 = imageElimina3;
                break;
            case 4:
                imageView = imageEquipo4;
                imageView1 = imageElimina4;
                break;
            case 5:
                imageView = imageEquipo5;
                imageView1 = imageElimina5;
                break;
            case 6:
                imageView = imageEquipo6;
                imageView1 = imageElimina6;
                break;
            case 7:
                imageView = imageEquipo7;
                imageView1 = imageElimina7;
                break;
            case 8:
                imageView = imageEquipo8;
                imageView1 = imageElimina8;
                break;
            default:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
        }
        equipos_s[num_image-1] = -1;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
        GraphicsUtil graphicUtil = new GraphicsUtil();
        // picView.setImageBitmap(graphicUtil.getRoundedShape(thePic,(float)1.5,92));
        imageView.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));
        imageView1.setVisibility(View.INVISIBLE);
    }

    private void setImage(int num_image){
        ImageView imageView = null,imageView1 = null;
        switch (num_image){
            case 1:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
                break;
            case 2:
                imageView = imageEquipo2;
                imageView1 = imageElimina2;
                break;
            case 3:
                imageView = imageEquipo3;
                imageView1 = imageElimina3;
                break;
            case 4:
                imageView = imageEquipo4;
                imageView1 = imageElimina4;
                break;
            case 5:
                imageView = imageEquipo5;
                imageView1 = imageElimina5;
                break;
            case 6:
                imageView = imageEquipo6;
                imageView1 = imageElimina6;
                break;
            case 7:
                imageView = imageEquipo7;
                imageView1 = imageElimina7;
                break;
            case 8:
                imageView = imageEquipo8;
                imageView1 = imageElimina8;
                break;
            default:
                imageView = imageEquipo1;
                imageView1 = imageElimina1;
        }

        int idImage = idEquipoDataF;
        File file = new File(gnr.local_dir_images+"equipos/"+idImage+".gif");
        showLog(file.getAbsolutePath());
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
                    gnr.endpoint_tournaments,
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
            VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
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
                    gnr.endpoint_teams+"&tournament_id="+idLiga,
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
            VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        }else{
            showLog("Pasando temp");
            showDialogEquipos(equipos);
            dialog.dismiss();
        }
    }

    private void showDialogLigas(final List<Liga> arrayList){
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_listview);
        // Set dialog title
        dialog.setTitle("");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,arrayList);
        final ListView listView = (ListView) dialog.findViewById(R.id.listview);
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

    private void showDialogEquipos(final List<Equipo> arrayList){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_listview);
        dialog.setTitle("");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,arrayList);
        final ListView listView = (ListView) dialog.findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idEquipo = arrayList.get(i).getId();
                idEquipoDataF = arrayList.get(i).getData_factory_id();
                buttonEquipos.setText(arrayList.get(i).getName());
                dialog.dismiss();
            }
        });
    }

    private void postFavoritos(JSONObject post){
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                gnr.endpoint_favorites+"/batch_update",
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONArray data = response.getJSONArray("response");
                            Intent intent = new Intent(Wizzard2Activity.this,Wizzard3Activity.class);
                            startActivity(intent);
                            finish();
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
                        params.put("Authorization", "Token " + gnr.getToken());
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}
