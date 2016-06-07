package com.advante.golazzos.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Interface.OnItemClickListener;
import com.advante.golazzos.Model.Jugada;
import com.advante.golazzos.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ruben Flores on 5/27/2016.
 */
public class List_Jugadas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private ArrayList<Jugada> items;
    private final OnItemClickListener listener;
    FragmentTransaction ft;

    public List_Jugadas(Activity context, ArrayList<Jugada> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View betsView = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case 1:
                betsView = inflater.inflate(R.layout.item_jugadas_1, parent, false);
                return new ViewHolder0(betsView);
            case 2:
                betsView = inflater.inflate(R.layout.item_jugadas_2, parent, false);
                return new ViewHolder0(betsView);
            default:
                betsView = inflater.inflate(R.layout.item_jugadas_1, parent, false);
                return new ViewHolder0(betsView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Jugada jugada = items.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm;

        ViewHolder0 viewHolder0 = (ViewHolder0) holder;
        viewHolder0.textEquipo1.setText(jugada.getEquipo1().getInitials());
        viewHolder0.textEquipo2.setText(jugada.getEquipo2().getInitials());
        viewHolder0.bind(items.get(position), listener);
        //String label = "Estas jugando al <font color='#0E5A80'>"+ jugada.getEquipo1().getName() +"</font> vs <font color='#0E5A80'>"+ jugada.getEquipo2().getName()+"</font>";

        viewHolder0.textLabel.setText(Html.fromHtml(jugada.getLabel()), TextView.BufferType.SPANNABLE);
        viewHolder0.textTime_ago.setText(jugada.getTextTime_ago());

        File file = new File(General.local_dir_images + "equipos/" + jugada.getEquipo1().getData_factory_id() + ".gif");
        if(file.exists()) {
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            viewHolder0.imageEquipo1.setImageBitmap(bm);
        }
        file = new File(General.local_dir_images + "equipos/" + jugada.getEquipo2().getData_factory_id() + ".gif");
        if(file.exists()) {
            bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            viewHolder0.imageEquipo2.setImageBitmap(bm);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        public TextView textEquipo1;
        public TextView textEquipo2;
        public TextView textTime_ago;
        public TextView textLabel;
        public ImageView imageEquipo1;
        public ImageView imageEquipo2;

        public ViewHolder0(View itemView) {
            super(itemView);

            textEquipo1  = (TextView) itemView.findViewById(R.id.textEquipo1);
            textEquipo2  = (TextView) itemView.findViewById(R.id.textEquipo2);
            textTime_ago = (TextView) itemView.findViewById(R.id.textTime_ago);
            textLabel    = (TextView) itemView.findViewById(R.id.textLabel);

            imageEquipo1 = (ImageView)itemView.findViewById(R.id.imageEquipo1);
            imageEquipo2 = (ImageView)itemView.findViewById(R.id.imageEquipo2);

        }

        public void bind(final Jugada item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
