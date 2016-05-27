package com.advante.golazzos.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.advante.golazzos.Model.User;
import com.advante.golazzos.R;

import java.io.File;

/**
 * Created by Ruben Flores on 3/31/2016.
 */
public class General {
    public static String appname = "Golazzos";
    public static String packetname = "com.advante.golazzos";
    private static Context context;
    public static String url_base;
    public static String endpoint_users;
    public static String endpoint_tokens;
    public static String endpoint_teams;
    public static String endpoint_tournaments;
    public static String endpoint_favorites;
    public static String endpoint_soul_team;
    public static String endpoint_point_charges;
    public static String endpoint_maches;
    public static String endpoint_maches_playing;
    public static String endpoint_maches_played;
    public static String endpoint_bets;
    public static String endpoint_posts;
    public static String endpoint_likes;
    public static String endpoint_ranking;
    public static String endpoint_users_search;
    public static String endpoint_friends;
    public static String endpoint_followers;
    public static String endpoint_weekly_awards;

    public static final String KEYWORD = "TESTSERVICE6";
    public static final String MEDIA = "inapp";

    public static final String local_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+appname+"/";
    public static final String local_dir_images = local_dir + "imagenes/";

    public static String[] pointsToBet = {"50", "100"};
    public static String[] pointsToWin = {"100", "200"};
    public static String[] betTypes = {"Marcador", "Gana/Pierde"};
    public static String[] resultTypes = {"GANA LOCAL", "GANA VISITANTE", "EMPATE"};

    private static String token;
    private static User loggedUser;


    public General(Context context) {
        General.context = context;
        url_base = context.getString(R.string.url_base_build);
        endpoint_users = url_base + context.getString(R.string.users_endpoint);
        endpoint_tokens = url_base + context.getString(R.string.tokens_endpoint);
        endpoint_teams = url_base + context.getString(R.string.teams_endpoint);
        endpoint_tournaments = url_base + context.getString(R.string.tournaments_endpoint);
        endpoint_favorites = url_base + context.getString(R.string.favorite_team_endpoint);
        endpoint_soul_team = url_base + context.getString(R.string.soul_team_endpoint);
        endpoint_point_charges = url_base + context.getString(R.string.point_charges_endpoint);
        endpoint_maches = url_base + context.getString(R.string.matches_endpoint);
        endpoint_maches_playing = url_base + context.getString(R.string.matches_live_endpoint);
        endpoint_maches_played = url_base + context.getString(R.string.matches_end_endpoint);
        endpoint_bets = url_base + context.getString(R.string.bet_endpoint);
        endpoint_posts = url_base + context.getString(R.string.posts_endpoint);
        endpoint_likes = url_base + context.getString(R.string.likes_endpoint);
        endpoint_ranking = url_base + context.getString(R.string.ranking_endpoint);
        endpoint_users_search = url_base + context.getString(R.string.users_search);
        endpoint_friends = url_base + context.getString(R.string.friends_endpoint);
        endpoint_followers = url_base + context.getString(R.string.followers_endpoint);
        endpoint_weekly_awards = url_base + context.getString(R.string.weekly_awards_endpoint);

        checkDirImages();
    }

    private void checkDirImages(){
        File file = new File(local_dir_images);
        if(!file.exists()){
            if(file.mkdirs())
                Log.i(appname,"Directories Created");
        }
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        General.token = token;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        General.loggedUser = loggedUser;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
