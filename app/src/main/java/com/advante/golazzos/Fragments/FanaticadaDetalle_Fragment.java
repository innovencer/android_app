package com.advante.golazzos.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.EstadisticasActivity;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Flores on 4/24/2016.
 */
public class FanaticadaDetalle_Fragment extends GeneralFragment {
    LinearLayout buttonBack,buttonFacebook,buttonTwitter,buttonLike,buttonEstadisticas,linearJugar,linearImageAttached;
    TextView textTime_ago,textLabel,textLike;
    ImageView imageEquipo1, imageViewAttached;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    String label,html_center_url,trackable_type, imageAttached;
    String pic_name;
    int id,idImage, idLike = -1;
    JsonObjectRequest jsArrayRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fanaticada_detalle, container, false);
        callbackManager = CallbackManager.Factory.create();

        buttonBack = (LinearLayout) view.findViewById(R.id.buttonBack);
        buttonFacebook = (LinearLayout) view.findViewById(R.id.buttonFacebook);
        buttonLike = (LinearLayout) view.findViewById(R.id.buttonLike);
        buttonEstadisticas = (LinearLayout) view.findViewById(R.id.buttonEstadisticas);
        buttonTwitter = (LinearLayout) view.findViewById(R.id.buttonTwitter);
        linearJugar = (LinearLayout) view.findViewById(R.id.linearJugar);
        linearImageAttached = (LinearLayout) view.findViewById(R.id.linearAttached);
        textLabel = (TextView) view.findViewById(R.id.textLabel);
        textTime_ago = (TextView) view.findViewById(R.id.textTime_ago);
        textLike = (TextView) view.findViewById(R.id.textLike);
        imageEquipo1 = (ImageView) view.findViewById(R.id.imageEquipo1);
        imageViewAttached = (ImageView) view.findViewById(R.id.imageAttached);

        Bundle bundle = this.getArguments();
        label = bundle.getString("label", "");
        html_center_url = bundle.getString("html_center_url", "");
        trackable_type = bundle.getString("trackable_type","");
        imageAttached = bundle.getString("imageAttached","");
        id = bundle.getInt("id", 0);
        idImage = bundle.getInt("idImage",0);
        idLike = bundle.getInt("like", -1);

        textLabel.setText(label);
        textTime_ago.setText(bundle.getString("time_ago",""));
        if(idLike > 0){
            textLike.setText("No me gusta");
        }else{
            textLike.setText("Me gusta");
        }
        if(html_center_url.isEmpty()){
            buttonEstadisticas.setVisibility(View.GONE);
        }else{
            buttonEstadisticas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), EstadisticasActivity.class);
                    intent.putExtra("html_center_url", html_center_url);
                    getContext().startActivity(intent);
                }
            });
        }
        if(!trackable_type.equals("bet")){
            linearJugar.setVisibility(View.GONE);
        }
        if(!imageAttached.isEmpty()){
            linearImageAttached.setVisibility(View.VISIBLE);
            pic_name = imageAttached.substring(0, imageAttached.lastIndexOf("/"));
            pic_name = pic_name.substring(pic_name.lastIndexOf("/")+1);

            File file = new File(General.local_dir_images + "posts/" + pic_name + ".png");
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                imageViewAttached.setImageBitmap(bm);
            } else {
                Picasso.with(getContext())
                        .load(imageAttached)
                        .error(android.R.drawable.ic_delete)
                        .placeholder(R.drawable.progress_animation)
                        .into(target);
            }
        }
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if (!bundle.getString("profile_pic_url","").contains("facebook.com")) {
            pic_name = bundle.getString("profile_pic_url", "").substring(
                    bundle.getString("profile_pic_url", "").lastIndexOf("/"),
                    bundle.getString("profile_pic_url", "").lastIndexOf("."));
        }else{
            pic_name = ""+idImage;
        }

        File file = new File(General.local_dir_images + "profile/" + pic_name + ".png");
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishFacebookFeed();
            }
        });
        buttonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishTwitterFeed();
            }
        });
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idLike>0) {
                    unlike();
                }else{
                    like();
                }
            }
        });

        return view;
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Bitmap bm = bitmap;
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageViewAttached.setImageBitmap(bm);
            FileOutputStream stream = null;
            File file;
            try {
                file = new File(General.local_dir_images + "posts/");
                if(!file.exists()){
                    file.mkdir();
                }
                stream = new FileOutputStream(General.local_dir_images + "posts/"+pic_name+".png");
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

    private void publishTwitterFeed(){
        TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
             .text(label);
        builder.show();
    }

    private void publishFacebookFeed(){

        Bundle params = new Bundle();
        JSONObject value = new JSONObject();
        try {
            value.put("value","SELF");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.putString("message", label);
        params.putString("link", getResources().getString(R.string.shareContentUrl));
        params.putString("privacy", value.toString());
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Toast.makeText(getContext(),"Mensaje compartido en facebook.",Toast.LENGTH_SHORT).show();
                    }
                }
        ).executeAsync();
        Toast.makeText(getContext(), "Compartiendo en facebook...", Toast.LENGTH_SHORT).show();
    }

    private void like(){
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                gnr.endpoint_posts+"/"+id+"/likes",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data = response.getJSONObject("response");
                            idLike = data.getInt("id");
                            textLike.setText("No me gusta");
                            showShortToast("Me gusta.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        for (String name: error.networkResponse.headers.keySet()){
                            String key =name.toString();
                            String value = error.networkResponse.headers.get(name).toString();
                            showLog(key + " " + value);
                        }
                        dialog.dismiss();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + gnr.getToken());
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }
    private void unlike(){
        showLog(gnr.endpoint_posts+"/"+id+"/likes/"+idLike);
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                gnr.endpoint_posts+"/"+id+"/likes/"+idLike,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        try {
                            JSONObject data = response.getJSONObject("response");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textLike.setText("Me gusta");
                        idLike = -1;
                        showShortToast("No me gusta.");
                        dialog.dismiss();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + gnr.getToken());
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }
}
