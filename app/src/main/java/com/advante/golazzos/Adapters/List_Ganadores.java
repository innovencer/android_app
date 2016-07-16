package com.advante.golazzos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.Ganador_Item;
import com.advante.golazzos.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ruben Flores on 5/3/2016.
 */
public class List_Ganadores extends ArrayAdapter<Ganador_Item> {
    ArrayList<Ganador_Item> _items;
    Context context;
    General gnr;
    View tempConvertView;
    public List_Ganadores(Context context, ArrayList<Ganador_Item> items) {
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
    public Ganador_Item getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        return _items.get(position).getType();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ganador_Item item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            switch (item.getType()){
                case 1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_aciertos_1, parent, false);
                    break;
                case 2:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_aciertos_2, parent, false);
                    break;
                case 3:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_aciertos_3, parent, false);
                    break;
                case 4:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_aciertos_4, parent, false);
                    break;
                case 5:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_aciertos_5, parent, false);
                    break;
            }

            holder = new ViewHolder();
            holder.imageEquipo1 = (ImageView) convertView.findViewById(R.id.imageEquipo1);
            holder.imageProfile = (ImageView) convertView.findViewById(R.id.imageProfile);
            holder.textPosicion = (TextView) convertView.findViewById(R.id.textPosicion);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.textEquipoAlma = (TextView) convertView.findViewById(R.id.textEquipoAlma);
            holder.textPremios = (TextView) convertView.findViewById(R.id.textPremios);
            holder.textAciertos = (TextView) convertView.findViewById(R.id.textAciertos);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        holder.textName.setText(item.getName());
        int pos = position+1;
        holder.textPosicion.setText(""+pos);
        holder.textPremios.setText(""+item.getPremios() +" pts");
        holder.textAciertos.setText(""+item.getScore());
        holder.textEquipoAlma.setText(item.getSouldTeamName());

        File file = new File(General.local_dir_images + "profile/no_profile.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        final GraphicsUtil graphicUtil = new GraphicsUtil();
        holder.imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));

        com.advante.golazzos.Helpers.Picasso.with(context).load("http:"+item.getPatchSoulTeam()).transform(new CircleTransform()).into(holder.imageEquipo1);
        com.advante.golazzos.Helpers.Picasso.with(context).load(item.getPatchProfileImage()).transform(new CircleTransform()).into(holder.imageProfile);
        holder.imageEquipo1.setVisibility(View.VISIBLE);

        return convertView;
    }

    static class ViewHolder {
        TextView textPosicion;
        TextView textName;
        TextView textEquipoAlma;
        TextView textPremios;
        TextView textAciertos;
        ImageView imageProfile;
        ImageView imageEquipo1;
    }
}
