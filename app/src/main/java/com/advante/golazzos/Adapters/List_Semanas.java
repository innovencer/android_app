package com.advante.golazzos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.advante.golazzos.Model.Ganador;
import com.advante.golazzos.R;

import java.util.List;

/**
 * Created by Ruben Flores on 5/15/2016.
 */
public class List_Semanas extends ArrayAdapter<Ganador> {
    List<Ganador> _items;
    Context context;

    public List_Semanas(Context context, List<Ganador> items) {
        super(context, R.layout.item_ranking_normal,items);
        this._items = items;
        this.context = context;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Ganador getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_semana, parent, false);
            holder = new ViewHolder();
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textName.setText(_items.get(position).getWeek_label());

        return convertView;
    }
    static class ViewHolder {
        TextView textName;
    }

}
