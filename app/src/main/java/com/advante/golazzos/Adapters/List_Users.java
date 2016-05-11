package com.advante.golazzos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Model.Ranking_Item;
import com.advante.golazzos.Model.SoulTeam;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.Model.UserBusqueda;
import com.advante.golazzos.Model.UserLevel;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 5/10/2016.
 */
public class List_Users extends ArrayAdapter<UserBusqueda> {
    ArrayList<UserBusqueda> _items;
    Context context;
    General gnr;
    JsonObjectRequest jsArrayRequest;
    public List_Users(Context context, ArrayList<UserBusqueda> items) {
        super(context, R.layout.item_ranking_normal,items);
        this._items = items;
        this.context = context;
        gnr = new General(context);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public UserBusqueda getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return _items.get(position).getType();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserBusqueda item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            switch (item.getType()){
                case 1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_busqueda_1, parent, false);
                    break;
                case 2:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_busqueda_2, parent, false);
                    break;
            }
            holder = new ViewHolder();
            holder.imageEquipo1 = (ImageView) convertView.findViewById(R.id.imageEquipo1);
            holder.imageProfile = (ImageView) convertView.findViewById(R.id.imageProfile);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.textEquipoAlma = (TextView) convertView.findViewById(R.id.textEquipoAlma);
            holder.linearAdd = (LinearLayout) convertView.findViewById(R.id.linearAdd);
            holder.linearInfo = (LinearLayout) convertView.findViewById(R.id.linearInfo);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        holder.textName.setText(item.getName());
        if(item.getSouldTeamName() != null) {
            holder.textEquipoAlma.setText(item.getSouldTeamName());
        }
        File file = new File(General.local_dir_images + "profile/no_profile.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        final GraphicsUtil graphicUtil = new GraphicsUtil();
        holder.imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));

        if(item.getPatchSoulTeam() != null) {
            String imagePath = item.getPatchSoulTeam().substring(item.getPatchSoulTeam().lastIndexOf("/") + 1, item.getPatchSoulTeam().lastIndexOf("-"));
            file = new File(General.local_dir_images + "equipos/" + imagePath + ".gif");
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
            holder.imageEquipo1.setVisibility(View.VISIBLE);
        }else{
            holder.imageEquipo1.setVisibility(View.INVISIBLE);
        }
        final String pic_name;
        if(item.getPatchProfileImage().contains("facebook.com")){
            pic_name = "" + item.getIdProfile();
        }else{
            pic_name = item.getPatchProfileImage().substring(
                    item.getPatchProfileImage().lastIndexOf("/"),
                    item.getPatchProfileImage().lastIndexOf("."));
        }

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap bm = bitmap;
                GraphicsUtil graphicUtil = new GraphicsUtil();
                holder.imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                        bm, 16));
                FileOutputStream stream = null;
                File file;
                try {
                    file = new File(General.local_dir_images + "profile/");
                    if(!file.exists()){
                        file.mkdir();
                    }
                    stream = new FileOutputStream(General.local_dir_images + "profile/"+pic_name+".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        file = new File(General.local_dir_images + "profile/" + pic_name + ".png");
        if (file.exists()) {
            try{
                options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                holder.imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                        bm, 100));
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Picasso.with(context)
                    .load(item.getPatchProfileImage())
                    .into(target);
        }

        holder.linearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend(item.getId());
            }
        });

        return convertView;
    }

    private void addFriend(int id){
        JSONObject friend = new JSONObject();
        JSONObject user_id = new JSONObject();
        try {
            user_id.put("user_id", id);
            friend.put("friend", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                gnr.endpoint_friends,
                friend,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        System.out.println("Golazzos "+response.toString());
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error en al tratar de conectar con el servicio web. Intente mas tarde", Toast.LENGTH_SHORT).show();
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

    static class ViewHolder {
        TextView textName;
        TextView textEquipoAlma;
        ImageView imageProfile;
        ImageView imageEquipo1;
        LinearLayout linearAdd;
        LinearLayout linearInfo;
    }
}
