package com.advante.golazzos.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.advante.golazzos.Model.Comentario;
import com.advante.golazzos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben Flores on 5/15/2016.
 */
public class List_BetPoints extends ArrayAdapter<String> {
    List<String> _items;
    Context context;

    public List_BetPoints(Context context, List<String> items) {
        super(context, R.layout.item_ranking_normal,items);
        this._items = items;
        this.context = context;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_points, parent, false);
            holder = new ViewHolder();
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textName.setText(_items.get(position));

        return convertView;
    }
    static class ViewHolder {
        TextView textName;
    }

}
