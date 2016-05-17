package com.advante.golazzos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.Comentario;
import com.advante.golazzos.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ruben Flores on 5/15/2016.
 */
public class List_Comentarios  extends ArrayAdapter<Comentario> {
    ArrayList<Comentario> _items;
    Context context;

    public List_Comentarios(Context context, ArrayList<Comentario> items) {
        super(context, R.layout.item_ranking_normal,items);
        this._items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Comentario getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comentario item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);
            holder = new ViewHolder();
            holder.imageProfile = (ImageView) convertView.findViewById(R.id.imageProfile);
            holder.textTimeAgo = (TextView) convertView.findViewById(R.id.textTime_ago);
            holder.textComentario = (TextView) convertView.findViewById(R.id.textComentario);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textTimeAgo.setText(item.getTime_ago());
        holder.textComentario.setText(Html.fromHtml("<strong>"+ item.getUser().getName() +"</strong> "+item.getText()));

        File file = new File(General.local_dir_images + "profile/no_profile.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        final GraphicsUtil graphicUtil = new GraphicsUtil();
        holder.imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));

        final String pic_name = ""+ item.getUser().getId();
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
                    .load(item.getUser().getProfile_pic_url())
                    .into(target);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textTimeAgo;
        TextView textComentario;
        ImageView imageProfile;
    }
}
