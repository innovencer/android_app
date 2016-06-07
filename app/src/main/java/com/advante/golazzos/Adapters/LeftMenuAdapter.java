package com.advante.golazzos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.advante.golazzos.Model.LeftMenu_Item;
import com.advante.golazzos.R;

import java.util.ArrayList;

/**
 * Created by Ruben Flores on 1/23/2016.
 */
public class LeftMenuAdapter extends ArrayAdapter<LeftMenu_Item> {
    ArrayList<LeftMenu_Item> items;

    public LeftMenuAdapter(Context context, ArrayList<LeftMenu_Item> _items) {
        super(context, R.layout.item_menu_type_one);
        items = _items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public LeftMenu_Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(LeftMenu_Item item) {
        return items.indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftMenu_Item leftMenu_item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_menu_type_one, parent, false);
        }
        //ImageView img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
        TextView text_item = (TextView) convertView.findViewById(R.id.text_item);

        //img_icon.setBackgroundResource(leftMenu_item.getIcon());
        text_item.setText(leftMenu_item.getText_item());

        return convertView;
    }
}
