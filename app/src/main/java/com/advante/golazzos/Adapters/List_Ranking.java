package com.advante.golazzos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.Ranking_Item;
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
public class List_Ranking  extends ArrayAdapter<Ranking_Item> {
    ArrayList<Ranking_Item> _items;
    Context context;
    General gnr;
    View tempConvertView;
    public List_Ranking(Context context, ArrayList<Ranking_Item> items) {
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
    public Ranking_Item getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return _items.get(position).getType();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ranking_Item item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            switch (item.getType()){
                case 1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_ranking_normal, parent, false);
                    break;
                case 2:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_ranking_highlight_green, parent, false);
                    break;
                case 3:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_ranking_highlight_green20, parent, false);
                    break;
                case 4:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_ranking_highlight_blue, parent, false);
                    break;
            }
            holder = new ViewHolder();
            holder.imageEquipo1 = (ImageView) convertView.findViewById(R.id.imageEquipo1);
            holder.imageProfile = (ImageView) convertView.findViewById(R.id.imageProfile);
            holder.textPosicion = (TextView) convertView.findViewById(R.id.textPosicion);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.textEquipoAlma = (TextView) convertView.findViewById(R.id.textEquipoAlma);
            holder.textNivel = (TextView) convertView.findViewById(R.id.textNivel);
            holder.textAciertos = (TextView) convertView.findViewById(R.id.textAciertos);
            holder.linearItemBackground = (LinearLayout) convertView.findViewById(R.id.linearItemBackground);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        holder.textName.setText(item.getName());
        int pos = position+1;
        holder.textPosicion.setText(""+pos);
        holder.textNivel.setText(""+item.getLevel());
        holder.textAciertos.setText(""+item.getAciertos());
        holder.textEquipoAlma.setText(item.getSouldTeamName());

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
            holder.imageEquipo1.setImageBitmap(bm);
            holder.imageEquipo1.setVisibility(View.VISIBLE);
        }else{
            holder.imageEquipo1.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_main));
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

        return convertView;
    }

    static class ViewHolder {
        TextView textPosicion;
        TextView textName;
        TextView textEquipoAlma;
        TextView textNivel;
        TextView textAciertos;
        ImageView imageProfile;
        ImageView imageEquipo1;
        LinearLayout linearItemBackground;
    }
}
