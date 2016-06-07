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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.Post;
import com.advante.golazzos.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ruben Flores on 4/24/2016.
 */
public class List_Posts extends ArrayAdapter<Post> {
    ArrayList<Post> _items;
    Context context;

    public List_Posts(Context context, ArrayList<Post> items) {
        super(context, R.layout.item_fanaticada_1,items);
        this._items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Post getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fanaticada_1, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageEquipo1 = (ImageView) convertView.findViewById(R.id.imageEquipo1);
            viewHolder.textTime_ago = (TextView) convertView.findViewById(R.id.textTime_ago);
            viewHolder.textLabel = (TextView) convertView.findViewById(R.id.textLabel);
            viewHolder.buttonGo = (LinearLayout) convertView.findViewById(R.id.buttonGo);

            convertView.setTag(viewHolder);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.textLabel.setText(Html.fromHtml(item.getLabel()));
        holder.textTime_ago.setText(item.getTime_ago());

        final String pic_name = ""+ item.getOwner().getId();

        File file = new File(General.local_dir_images + "profile/no_profile.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        GraphicsUtil graphicUtil = new GraphicsUtil();
        holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap bm = bitmap;
                GraphicsUtil graphicUtil = new GraphicsUtil();
                holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
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
                holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                        bm, 100));
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Picasso.with(context)
                    .load(item.getOwner().getProfile_pic_url())
                    .into(target);
        }

        return convertView;
    }


    static class ViewHolder {
        ImageView imageEquipo1;
        TextView textTime_ago;
        TextView textLabel;
        LinearLayout buttonGo;
    }

}
