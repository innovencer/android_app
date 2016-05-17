package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Posts;
import com.advante.golazzos.Adapters.List_Ranking;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Like;
import com.advante.golazzos.Model.Owner;
import com.advante.golazzos.Model.Post;
import com.advante.golazzos.Model.Ranking_Item;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking1, container, false);
        listView = (ListView) view.findViewById(R.id.listview);

        linear1 = (LinearLayout) view.findViewById(R.id.linear1);
        linear2 = (LinearLayout) view.findViewById(R.id.linear2);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);
        linear4 = (LinearLayout) view.findViewById(R.id.linear4);

        linear1.setOnClickListener(clickTab);
        linear2.setOnClickListener(clickTab);
        linear3.setOnClickListener(clickTab);
        linear4.setOnClickListener(clickTab);

        getRanking("");
        return view;
    }

    View.OnClickListener clickTab = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().equals("1")){
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.INVISIBLE);
                getRanking("");
            }else{
                linear3.setVisibility(View.INVISIBLE);
                linear4.setVisibility(View.VISIBLE);
                getRanking("?weekly=true");
            }
        }
    };

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
                            List_Ranking list = new List_Ranking(getContext(),ranking_items);
                            listView.setAdapter(list);
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

}
