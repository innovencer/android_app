package com.advante.golazzos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.advante.golazzos.Adapters.List_Posts;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Like;
import com.advante.golazzos.Model.Owner;
import com.advante.golazzos.Model.Post;
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
 * Created by Ruben Flores on 4/24/2016.
 */
public class Fanaticada_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    ListView listView;
    LinearLayout buttonComentar, buttonCamera, linearComentar;
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
        View view = inflater.inflate(R.layout.fragment_fanaticada, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_post,null);
        ((ViewGroup)listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        linearComentar = (LinearLayout) view.findViewById(R.id.linearComentar);

        linearComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContent, new FanaticadaEscribir_Fragment(), "");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        if(flag) {
            buscarPosts();
        }
        return view;
    }

    private void buscarPosts(){
        dialog.show();
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                General.endpoint_posts,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONArray data = response.getJSONArray("response");
                            final ArrayList<Post> posts = new ArrayList<>();
                            Post post;
                            Owner owner;
                            Equipo equipo;
                            for(int i=0;i<data.length();i++){
                                post = new Post();
                                post.setId(data.getJSONObject(i).getInt("id"));
                                post.setTime_ago(data.getJSONObject(i).getString("time_ago"));
                                if(data.getJSONObject(i).has("image_url"))
                                    post.setImage_url(data.getJSONObject(i).getString("image_url"));

                                owner = new Owner();
                                owner.setId(data.getJSONObject(i).getJSONObject("owner").getInt("id"));
                                owner.setName(data.getJSONObject(i).getJSONObject("owner").getString("name"));
                                /*
                                if(owner.getId() == gnr.getLoggedUser().getId())
                                    post.setLabel(data.getJSONObject(i).getString("mobile_label").replace("<team>","<font color='#0E5A80'>").replace("</team>","</font>"));
                                else*/
                                post.setLabel("<font color='#0E5A80'>"+ owner.getName()+"</font> "+data.getJSONObject(i).getString("mobile_label").replace("<team>","<font color='#0E5A80'>").replace("</team>","</font>"));
                                owner.setEmail(data.getJSONObject(i).getJSONObject("owner").getString("email"));
                                owner.setProfile_pic_url(data.getJSONObject(i).getJSONObject("owner").getString("profile_pic_url"));

                                equipo = new Equipo();
                                if(data.getJSONObject(i).getJSONObject("owner").has("soul_team") && !data.getJSONObject(i).getJSONObject("owner").isNull("soul_team")) {
                                    equipo.setId(data.getJSONObject(i).getJSONObject("owner").getJSONObject("soul_team").getInt("id"));
                                    equipo.setData_factory_id(data.getJSONObject(i).getJSONObject("owner").getJSONObject("soul_team").getInt("data_factory_id"));
                                    equipo.setName(data.getJSONObject(i).getJSONObject("owner").getJSONObject("soul_team").getString("name"));
                                    equipo.setInitials(data.getJSONObject(i).getJSONObject("owner").getJSONObject("soul_team").getString("initials"));
                                    equipo.setImage_path(data.getJSONObject(i).getJSONObject("owner").getJSONObject("soul_team").getString("image_path"));
                                    equipo.setCountry_name(data.getJSONObject(i).getJSONObject("owner").getJSONObject("soul_team").getString("country_name"));
                                }
                                JSONArray likes = data.getJSONObject(i).getJSONArray("likes");
                                Like like;
                                ArrayList<Like> likes1= new ArrayList<>();
                                for (int x=0; x < likes.length();x++){
                                    like = new Like(likes.getJSONObject(x).getInt("id"),likes.getJSONObject(x).getString("likeable_type"),likes.getJSONObject(x).getBoolean("mine"));
                                    if (like.getMine())
                                        post.setLikedByMe(like.getId());
                                    likes1.add(like);
                                }

                                owner.setSoul_team(equipo);
                                post.setOwner(owner);
                                post.setLikes(likes1);

                                String trackable_type = "";
                                if(data.getJSONObject(i).getJSONObject("trackable").has("trackable_type")){
                                    trackable_type = data.getJSONObject(i).getJSONObject("trackable").getString("trackable_type");
                                    post.setTrackable_type(trackable_type);
                                    if(trackable_type.equals("match")){
                                        if(data.getJSONObject(i).getJSONObject("trackable").has("match"))
                                            post.setHtml_center_url(data.getJSONObject(i).getJSONObject("trackable").getString("html_center_url"));
                                    }else if(trackable_type.equals("bet")){
                                        if(data.getJSONObject(i).getJSONObject("trackable").getJSONObject("match").has("html_center_url"))
                                            post.setHtml_center_url(data.getJSONObject(i).getJSONObject("trackable").getJSONObject("match").getString("html_center_url"));
                                    }
                                    if(data.getJSONObject(i).getJSONObject("trackable").has("match"))
                                        post.setIdPartido(data.getJSONObject(i).getJSONObject("trackable").getJSONObject("match").getInt("id"));
                                }

                                posts.add(post);
                            }

                            List_Posts list_posts = new List_Posts(getContext(),posts);
                            listView.setAdapter(list_posts);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    FanaticadaDetalle_Fragment fragment = new FanaticadaDetalle_Fragment();
                                    Bundle bundle = new Bundle();
                                    Post post1 = posts.get(i);
                                    bundle.putString("time_ago", post1.getTime_ago());
                                    bundle.putString("label", post1.getLabel());
                                    bundle.putString("profile_pic_url", post1.getOwner().getProfile_pic_url());
                                    bundle.putString("html_center_url", post1.getHtml_center_url());
                                    bundle.putString("trackable_type", post1.getTrackable_type());
                                    bundle.putString("imageAttached",post1.getImage_url());
                                    bundle.putString("idPartido",""+post1.getIdPartido());
                                    bundle.putInt("idImage", post1.getOwner().getId());
                                    bundle.putInt("like",post1.getLikedByMe());
                                    bundle.putInt("id",post1.getId());

                                    fragment.setArguments(bundle);
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.flContent, fragment, "");
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }
                            });
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
}
