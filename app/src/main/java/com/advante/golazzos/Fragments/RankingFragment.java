package com.advante.golazzos.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Ranking;
import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.Picasso;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.Ranking_Item;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/3/2016.
 */
public class RankingFragment extends GeneralFragment {
    JsonArrayRequest jsArrayRequest;
    ListView listView;
    LinearLayout linear1, linear2, linear3, linear4;
    TextView textPosicion, textName, textEquipoAlma, textNivel, textAciertos;
    ImageView image1, image2, imageProfile, imageEquipo1;
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
        View view = inflater.inflate(R.layout.fragment_ranking1, container, false);
        if(flag) {
            listView = (ListView) view.findViewById(R.id.listview);

            linear1 = (LinearLayout) view.findViewById(R.id.linear1);
            linear2 = (LinearLayout) view.findViewById(R.id.linear2);
            linear3 = (LinearLayout) view.findViewById(R.id.linear3);
            linear4 = (LinearLayout) view.findViewById(R.id.linear4);

            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
            imageEquipo1 = (ImageView) view.findViewById(R.id.imageEquipo1);

            linear1.setOnClickListener(clickTab);
            linear2.setOnClickListener(clickTab);
            linear3.setOnClickListener(clickTab);
            linear4.setOnClickListener(clickTab);

            textName = (TextView) view.findViewById(R.id.textName);
            textAciertos = (TextView) view.findViewById(R.id.textAciertos);
            textPosicion = (TextView) view.findViewById(R.id.textPosicion);
            textEquipoAlma = (TextView) view.findViewById(R.id.textEquipoAlma);
            textNivel = (TextView) view.findViewById(R.id.textNivel);

            textName.setText(gnr.getLoggedUser().getName());
            //textAciertos.setText("" + gnr.getLoggedUser().getScore());
            //textPosicion.setText("" + gnr.getLoggedUser().getRank());
            textEquipoAlma.setText(gnr.getLoggedUser().getSoul_team().getName());
            textNivel.setText("" + gnr.getLoggedUser().getLevel().getOrder());

            gnr.getUser(new IGetUser_Listener() {
                @Override
                public void onComplete(Boolean complete, User user) {
                    if (complete) {
                        init();
                    }
                }
            });
            getRanking("?weekly=true");
        }
        return view;
    }

    View.OnClickListener clickTab = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().equals("1")){
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.INVISIBLE);
                textAciertos.setText("" + gnr.getLoggedUser().getScore2());
                textPosicion.setText("" + gnr.getLoggedUser().getRank2());
                getRanking("?weekly=true");
            }else{
                image1.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.VISIBLE);
                textAciertos.setText("" + gnr.getLoggedUser().getScore());
                textPosicion.setText("" + gnr.getLoggedUser().getRank());
                getRanking("");
            }
        }
    };

    private void init(){
        textAciertos.setText("" + gnr.getLoggedUser().getScore2());
        textPosicion.setText("" + gnr.getLoggedUser().getRank2());
        Picasso.with(getContext()).load(gnr.getLoggedUser().getProfile_pic_url()).transform(new CircleTransform()).into(imageProfile);
        String imagePath = gnr.getLoggedUser().getSoul_team().getImage_path();
        int idImage = Integer.parseInt(imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.lastIndexOf("-")));

        File file;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm;
        GraphicsUtil graphicUtil = new GraphicsUtil();
        file = new File(General.local_dir_images + "equipos/" + idImage + ".gif");
        if(file.exists()) {
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }
    }

    private void getRanking(String type){
        dialog.show();
        jsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                General.endpoint_ranking +type,
                "",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data;
                            Ranking_Item ranking_item;
                            ArrayList<Ranking_Item> ranking_items = new ArrayList<>();

                            for(int i=0;i<response.length();i++){
                                data = response.getJSONObject(i);
                                ranking_item = new Ranking_Item();
                                ranking_item.setIdProfile(data.getInt("rank"));
                                ranking_item.setName(data.getJSONObject("member_data").getJSONObject("user").getString("name"));
                                ranking_item.setIdProfile(data.getJSONObject("member_data").getJSONObject("user").getInt("id"));
                                ranking_item.setPatchProfileImage(data.getJSONObject("member_data").getJSONObject("user").getString("profile_pic_url"));
                                if(data.getJSONObject("member_data").getJSONObject("user").has("soul_team")) {
                                    ranking_item.setPatchSoulTeam(data.getJSONObject("member_data").getJSONObject("user").getJSONObject("soul_team").getString("image_path"));
                                    ranking_item.setSouldTeamName(data.getJSONObject("member_data").getJSONObject("user").getJSONObject("soul_team").getString("name"));
                                }
                                ranking_item.setAciertos(data.getInt("score"));
                                ranking_item.setLevel(data.getJSONObject("member_data").getJSONObject("user").getJSONObject("level").getInt("order"));

                                if(i<2){
                                    ranking_item.setType(2);
                                }else if(i==2){
                                    ranking_item.setType(3);
                                }else{
                                    ranking_item.setType(1);
                                }
                                ranking_items.add(ranking_item);
                            }
                            if(ranking_items.size() <= 0){
                                View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_match_1,null);
                                ((ViewGroup)listView.getParent()).addView(emptyView);
                                listView.setEmptyView(emptyView);
                            }else {
                                List_Ranking list = new List_Ranking(getContext(), ranking_items);
                                listView.setAdapter(list);
                            }
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

}
