package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/24/2016.
 */
public class Owner extends Model {
    int id;
    String name;
    String email;
    String profile_pic_url;
    Equipo soul_team;

    public Owner() {

    }

    public Owner(int id, String name, String email, String profile_pic_url, Equipo soul_team) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile_pic_url = profile_pic_url;
        this.soul_team = soul_team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public Equipo getSoul_team() {
        return soul_team;
    }

    public void setSoul_team(Equipo soul_team) {
        this.soul_team = soul_team;
    }
}
