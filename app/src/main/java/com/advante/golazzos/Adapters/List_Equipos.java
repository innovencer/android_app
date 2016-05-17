package com.advante.golazzos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.Equipo;
import com.advante.golazzos.Model.Post;
import com.advante.golazzos.Model.Ranking_Item;
import com.advante.golazzos.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ruben Flores on 5/15/2016.
 */
public class List_Equipos extends ArrayAdapter<Equipo> {
    ArrayList<Equipo> _items;
    Context context;

    public List_Equipos(Context context, ArrayList<Equipo> items) {
        super(context, R.layout.item_ranking_normal,items);
        this._items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Equipo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Equipo item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog, parent, false);
            holder = new ViewHolder();
            holder.imageEquipo1 = (ImageView) convertView.findViewById(R.id.imageEquipo1);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textName.setText(item.getName() + " ("+ item.getInitials()+")");

        String imagePath = item.getImage_path();
        imagePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.lastIndexOf("-"));
        File file = new File(General.local_dir_images + "equipos/" + imagePath + ".gif");

        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textName;
        ImageView imageEquipo1;
    }
}
