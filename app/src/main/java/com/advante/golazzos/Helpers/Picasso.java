package com.advante.golazzos.Helpers;

import android.content.Context;
import android.widget.ImageView;

import com.advante.golazzos.R;
import com.squareup.picasso.Transformation;

/**
 * Created by Ruben Flores on 7/15/2016.
 */
public class Picasso {
    private static Picasso singleton;
    private static Context context;
    private static String  path;
    private static Transformation transformation;

    private Picasso(Context context) {
        this.context = context;
    }

    public static synchronized Picasso with(Context context) {
        if (singleton == null) {
            singleton = new Picasso(context);
        }
        return singleton;
    }

    public Picasso load(String path){
        this.path = path;
        return singleton;
    }

    public Picasso transform(Transformation transformation){
        this.transformation = transformation;
        return singleton;
    }

    public void into(ImageView imageView){
        if(transformation == null)
            com.squareup.picasso.Picasso.with(context).load(this.path)
                                    .error(R.drawable.ic_main)
                                    .placeholder(R.drawable.ic_main)
                                    .into(imageView);
        else
            com.squareup.picasso.Picasso.with(context).load(this.path).transform(transformation)
                                    .error(R.drawable.ic_main)
                                    .placeholder(R.drawable.ic_main)
                                    .into(imageView);
    }

}
