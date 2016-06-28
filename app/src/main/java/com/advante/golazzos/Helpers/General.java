package com.advante.golazzos.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.advante.golazzos.Interface.IBuscarLigas;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.Counters;
import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.Model.UserLevel;
import com.advante.golazzos.Model.UserSettings;
import com.advante.golazzos.PrincipalActivity;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruben Flores on 3/31/2016.
 */
public class General {
    public static String appname = "Golazzos";
    public static String packetname = "com.advante.golazzos";
    private static Context context;
    public static String url_base;
    public static String endpoint_users;
    public static String endpoint_tokens;
    public static String endpoint_teams;
    public static String endpoint_tournaments;
    public static String endpoint_favorites;
    public static String endpoint_soul_team;
    public static String endpoint_point_charges;
    public static String endpoint_maches;
    public static String endpoint_maches_playing;
    public static String endpoint_maches_played;
    public static String endpoint_bets;
    public static String endpoint_posts;
    public static String endpoint_likes;
    public static String endpoint_ranking;
    public static String endpoint_users_search;
    public static String endpoint_friends;
    public static String endpoint_followers;
    public static String endpoint_weekly_awards;
    public static String endpoint_subscription;
    public static String endpoint_password;

    public static final String KEYWORD = "TESTSERVICE6";
    public static final String KEYWORD_UNS= "TESTSERVICE6BAJA";
    public static final String MEDIA = "inapp";

    public static final String local_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+appname+"/";
    public static final String local_dir_images = local_dir + "imagenes/";

    public static String[] pointsToBet = {"50", "100"};
    public static String[] pointsToWin = {"100", "200"};
    public static String[] betTypes = {"Marcador", "Gana/Pierde"};
    public static String[] resultTypes = {"GANA LOCAL", "GANA VISITANTE", "EMPATE"};

    private static String token;
    private static User loggedUser;

    public static String locale;

    private static List<Fragment> lastFragment = new ArrayList<>();

    private ProgressDialog dialog;

    JsonObjectRequest jsArrayRequest;

    ArrayList<Liga> ligas = null;
    IBuscarLigas iBuscarLigas;

    public General(Context context) {
        General.context = context;
        url_base = context.getString(R.string.url_base_build);
        endpoint_users = url_base + context.getString(R.string.users_endpoint);
        endpoint_tokens = url_base + context.getString(R.string.tokens_endpoint);
        endpoint_teams = url_base + context.getString(R.string.teams_endpoint);
        endpoint_tournaments = url_base + context.getString(R.string.tournaments_endpoint);
        endpoint_favorites = url_base + context.getString(R.string.favorite_team_endpoint);
        endpoint_soul_team = url_base + context.getString(R.string.soul_team_endpoint);
        endpoint_point_charges = url_base + context.getString(R.string.point_charges_endpoint);
        endpoint_maches = url_base + context.getString(R.string.matches_endpoint);
        endpoint_maches_playing = url_base + context.getString(R.string.matches_live_endpoint);
        endpoint_maches_played = url_base + context.getString(R.string.matches_end_endpoint);
        endpoint_bets = url_base + context.getString(R.string.bet_endpoint);
        endpoint_posts = url_base + context.getString(R.string.posts_endpoint);
        endpoint_likes = url_base + context.getString(R.string.likes_endpoint);
        endpoint_ranking = url_base + context.getString(R.string.ranking_endpoint);
        endpoint_users_search = url_base + context.getString(R.string.users_search);
        endpoint_friends = url_base + context.getString(R.string.friends_endpoint);
        endpoint_followers = url_base + context.getString(R.string.followers_endpoint);
        endpoint_weekly_awards = url_base + context.getString(R.string.weekly_awards_endpoint);
        endpoint_subscription = url_base + context.getString(R.string.subscription_endpoint);
        endpoint_password = url_base + context.getString(R.string.password_endpoint);;
        locale = context.getResources().getConfiguration().locale.getISO3Country();
        dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setMessage("Conectando...");
        checkDirImages();
    }

    private void checkDirImages(){
        File file = new File(local_dir_images);
        if(!file.exists()){
            if(file.mkdirs())
                Log.i(appname,"Directories Created");
        }
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        General.token = token;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        General.loggedUser = loggedUser;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public void buscarLigas(IBuscarLigas buscarligas){
        this.iBuscarLigas = buscarligas;
        dialog.show();
        ligas = null;
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
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        iBuscarLigas.onComplete(ligas);
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
                        iBuscarLigas.onComplete(ligas);
                    }
                });
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }

    public static Fragment getLastFragment() {
        if(lastFragment.size()>1){
            return lastFragment.get(lastFragment.size()-2);
        }else{
            return null;
        }
    }

    public static void setLastFragment(Fragment lastFragment) {
        General.lastFragment.add(lastFragment);
    }

    public void clearHistoryFragment(){
        General.lastFragment.clear();
    }

    public void getUser(){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_users +"/me",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        User user1 = new User();
                        try {
                            JSONObject data = response.getJSONObject("response");
                            user1.setEmail(data.getString("email"));
                            user1.setId(data.getInt("id"));
                            user1.setName(data.optString("name"));
                            user1.setPaid_subscription(data.getBoolean("paid_subscription"));
                            user1.setSubscription_id(data.getString("subscription_id"));
                            user1.setPoints(data.getDouble("points"));
                            user1.setProfile_pic_url(data.getString("profile_pic_url"));

                            if(!data.isNull("soul_team")){
                                JSONObject soul_team = data.getJSONObject("soul_team");
                                user1.setSoul_team(new SoulTeam(soul_team.getString("image_path"), soul_team.getString("name"), soul_team.getInt("id")));
                            }

                            user1.setScore(data.getJSONObject("ranking").getInt("score"));
                            user1.setRank(data.getJSONObject("ranking").getInt("rank"));

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
                            boolean friendship_notification = false;
                            if(!data.getJSONObject("settings").has("friendship_notification")){
                                friendship_notification = true;
                            }else{
                                friendship_notification = data.getJSONObject("settings").getBoolean("friendship_notification");
                            }
                            user1.setUserSettings(new UserSettings(
                                    data.getJSONObject("settings").getBoolean("won_notification"),
                                    data.getJSONObject("settings").getBoolean("lose_notification"),
                                    data.getJSONObject("settings").getBoolean("new_bet_notification"),
                                    friendship_notification,
                                    data.getJSONObject("settings").getBoolean("closed_match_notification")));
                            setLoggedUser(user1);

                            dialog.dismiss();
                            try{
                                File myFile = new File(local_dir+"tests.txt");
                                myFile.createNewFile();
                                FileOutputStream fOut = new FileOutputStream(myFile);
                                OutputStreamWriter myOutWriter =
                                        new OutputStreamWriter(fOut);
                                myOutWriter.append(General.getToken());
                                myOutWriter.close();
                                fOut.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"Error al conectar al servicio",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        /*
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
                        */
                        dialog.dismiss();
                        Toast.makeText(context,"Error al conectar al servicio",Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }
}
