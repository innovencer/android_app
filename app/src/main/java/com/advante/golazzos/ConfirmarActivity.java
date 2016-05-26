package com.advante.golazzos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Partidos;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.Model.Partido;
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
 * Created by Ruben Flores on 4/23/2016.
 */
public class ConfirmarActivity extends GeneralActivity {
    TextView textTipoJugada,textResultado,textResultadoLocal,textNombreLocal,textResultadoVisitante,textNombreVisitante;
    ImageView imageEquipo1;
    LinearLayout buttonConfirmar, buttonBack;
    Intent intent;

    int idPartido,amount_centavos,local_score,visitant_score,bet_option_id;
    String nombreLocal,nombreVisitante,imagePath;

    JsonObjectRequest jsArrayRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);
        initUI();
    }

    private void initUI(){
        intent = getIntent();

        textTipoJugada = (TextView) findViewById(R.id.textTipoJugada);
        textResultado = (TextView) findViewById(R.id.textResultado);
        textResultadoLocal = (TextView) findViewById(R.id.textResultadoLocal);
        textNombreLocal = (TextView) findViewById(R.id.textNombreLocal);
        textResultadoVisitante = (TextView) findViewById(R.id.textResultadoVisitante);
        textNombreVisitante = (TextView) findViewById(R.id.textNombreVisitante);

        imageEquipo1 = (ImageView) findViewById(R.id.imageEquipo1);

        buttonConfirmar = (LinearLayout) findViewById(R.id.buttonConfirmar);
        buttonBack = (LinearLayout) findViewById(R.id.buttonBack);

        idPartido = intent.getIntExtra("match_id",0);
        amount_centavos = intent.getIntExtra("amount_centavos",0)*100;
        local_score = intent.getIntExtra("local_score",0);
        visitant_score = intent.getIntExtra("visitant_score",0);
        bet_option_id = intent.getIntExtra("bet_option_id", 0);
        nombreLocal = intent.getStringExtra("nombreLocal");
        nombreVisitante = intent.getStringExtra("nombreVisitante");
        imagePath = intent.getStringExtra("image");

        if(bet_option_id>0){
            textResultadoVisitante.setVisibility(View.INVISIBLE);
            textResultadoLocal.setVisibility(View.INVISIBLE);
        }

        textTipoJugada.setText(General.betTypes[bet_option_id]);
        if(local_score>visitant_score){
            textResultado.setText("GANA LOCAL");
        }else if(local_score < visitant_score){
            textResultado.setText("GANA VISITANTE");
        }else if(local_score == visitant_score){
            textResultado.setText("EMPATE");
        }

        textResultadoLocal.setText(""+local_score);
        textResultadoVisitante.setText(""+visitant_score);
        textNombreLocal.setText(nombreLocal);
        textNombreVisitante.setText(nombreVisitante);


        File file = new File(General.local_dir_images + "equipos/" + imagePath + ".gif");
        showLog(imagePath);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmarJugada();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ConfirmarJugada(){
        dialog.show();
        JSONObject bet = new JSONObject();
        JSONObject values = new JSONObject();
        try {
            values.put("match_id", idPartido);
            values.put("amount_centavos",amount_centavos);
            values.put("local_score",local_score);
            values.put("visitant_score",visitant_score);
            //Esto esta asi por temas de back, no pueden existir otro tipo de apuestas que no tengan
            //bet_options.
            if(bet_option_id>0)
                values.put("bet_option_id", bet_option_id);
            bet.put("bet",values);
            showLog(bet.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_bets,
                bet,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Dialog dialog1 = new Dialog(ConfirmarActivity.this,android.R.style.Theme_DeviceDefault_Dialog);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialog_confirmar_jugada);
                        dialog1.setCancelable(false);

                        ImageView image = (ImageView) dialog1.findViewById(R.id.image);

                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                        dialog1.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConfirmarActivity.this,"Error en al tratar de conectar con el servicio web. Intente mas tarde",Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}
