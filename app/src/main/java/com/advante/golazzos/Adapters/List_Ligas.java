package com.advante.golazzos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.Picasso;
import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.R;

import java.util.ArrayList;

/**
 * Created by Ruben Flores on 5/15/2016.
 */
public class List_Ligas extends ArrayAdapter<Liga> {
    ArrayList<Liga> _items;
    Context context;

    public List_Ligas(Context context, ArrayList<Liga> items) {
        super(context, R.layout.item_ranking_normal,items);
        this._items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Liga getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Liga item = getItem(position);
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
        holder.textName.setText(item.getName());

        Picasso.with(context).load(item.getLogo()).transform(new CircleTransform()).into(holder.imageEquipo1);

        return convertView;
    }

    static class ViewHolder {
        TextView textName;
        ImageView imageEquipo1;
    }
}
