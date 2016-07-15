package com.advante.golazzos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralActivity;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Counters;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.Model.UserLevel;
import com.advante.golazzos.Model.UserSettings;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

        if(local_score != visitant_score) {
            File file = new File(General.local_dir_images + "equipos/" + imagePath + ".gif");
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
        }else{
            imageEquipo1.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main));
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
            //Esto esta asi por temas de back, no pueden existir otro tipo de apuestas que no tengan
            //bet_options.
            if(bet_option_id>0) {
                if(local_score > visitant_score){
                    values.put("bet_option_id", 1);
                }else if(visitant_score > local_score){
                    values.put("bet_option_id", 2);
                }else if( local_score == visitant_score){
                    values.put("bet_option_id", 3);
                }
            }else{
                values.put("local_score",local_score);
                values.put("visitant_score",visitant_score);
            }
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
                        User user1 = new User();
                        try {
                            JSONObject data = response.getJSONObject("current_user");
                            user1.setEmail(data.getString("email"));
                            user1.setId(data.getInt("id"));
                            user1.setName(data.optString("name"));
                            user1.setPaid_subscription(data.getBoolean("paid_subscription"));
                            user1.setPoints(data.getDouble("points"));
                            user1.setProfile_pic_url(data.getString("profile_pic_url"));

                            if(!data.isNull("soul_team")){
                                JSONObject soul_team = data.getJSONObject("soul_team");
                                user1.setSoul_team(new SoulTeam(soul_team.getString("image_path"), soul_team.getString("name"), soul_team.getInt("id")));
                            }

                            JSONObject level = data.getJSONObject("level");
                            user1.setLevel(new UserLevel(level.getInt("hits_count"), level.getString("logo_url"),
                                    level.getString("name"), level.getInt("order"),level.getInt("points")));

                            int marcadorTotal_bets = 0, marcadorWon_bets = 0, ganaPierdeWon_bets = 0, ganaPierdeTotal_bets = 0,
                                    total_bets = 0, won_bets = 0, diferenciaGoleTotal_bets = 0, diferenciaGolesWon_bets =0,
                                    primerGolTotal_bets = 0, primerGolWon_bets = 0, numeroGolesTotal_bets = 0, numeroGolesWon_bets = 0;
                            if(data.getJSONObject("counters").has("Marcador"))
                                marcadorTotal_bets = data.getJSONObject("counters").getJSONObject("Marcador").getInt("total_bets");
                            if(data.getJSONObject("counters").has("Marcador"))
                                marcadorWon_bets = data.getJSONObject("counters").getJSONObject("Marcador").getInt("won_bets");
                            if(data.getJSONObject("counters").has("Gana/Pierde"))
                                ganaPierdeTotal_bets = data.getJSONObject("counters").getJSONObject("Gana/Pierde").getInt("total_bets");
                            if(data.getJSONObject("counters").has("Gana/Pierde"))
                                ganaPierdeWon_bets = data.getJSONObject("counters").getJSONObject("Gana/Pierde").getInt("won_bets");
                            if(data.getJSONObject("counters").has("Total"))
                                total_bets = data.getJSONObject("counters").getJSONObject("Total").getInt("total_bets");
                            if(data.getJSONObject("counters").has("Total"))
                                won_bets = data.getJSONObject("counters").getJSONObject("Total").getInt("won_bets");
                            if(data.getJSONObject("counters").has("Diferencia de goles")){
                                diferenciaGoleTotal_bets = data.getJSONObject("counters").getJSONObject("Diferencia de goles").getInt("total_bets");
                                diferenciaGolesWon_bets = data.getJSONObject("counters").getJSONObject("Diferencia de goles").getInt("won_bets");
                            }
                            if(data.getJSONObject("counters").has("Primer Gol")){
                                primerGolTotal_bets = data.getJSONObject("counters").getJSONObject("Primer Gol").getInt("total_bets");
                                primerGolWon_bets = data.getJSONObject("counters").getJSONObject("Primer Gol").getInt("won_bets");
                            }
                            if(data.getJSONObject("counters").has("No. de Goles")){
                                numeroGolesTotal_bets = data.getJSONObject("counters").getJSONObject("No. de Goles").getInt("total_bets");
                                numeroGolesWon_bets = data.getJSONObject("counters").getJSONObject("No. de Goles").getInt("won_bets");
                            }
                            user1.setCounters(new Counters(
                                    marcadorTotal_bets,
                                    marcadorWon_bets,
                                    ganaPierdeTotal_bets,
                                    ganaPierdeWon_bets,
                                    diferenciaGoleTotal_bets,
                                    diferenciaGolesWon_bets,
                                    primerGolTotal_bets,
                                    primerGolWon_bets,
                                    numeroGolesTotal_bets,
                                    numeroGolesWon_bets,
                                    total_bets,
                                    won_bets
                            ));
                            /*
                            user1.setUserSettings(new UserSettings(
                                    data.getJSONObject("settings").getBoolean("won_notification"),
                                    data.getJSONObject("settings").getBoolean("lose_notification"),
                                    data.getJSONObject("settings").getBoolean("new_bet_notification"),
                                    false,
                                    data.getJSONObject("settings").getBoolean("closed_match_notification")));
                            */
                            user1.setUserSettings(new UserSettings(
                                    false,
                                    false,
                                    false,
                                    false,
                                    false));
                            gnr.setLoggedUser(user1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
