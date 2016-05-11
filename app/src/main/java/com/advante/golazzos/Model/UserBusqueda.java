package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/10/2016.
 */
public class UserBusqueda extends User {
    int type = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSouldTeamName(){
        return this.getSoul_team().name;
    }

    public String getPatchSoulTeam() {
        return this.getSoul_team().getImage_path();
    }

    public String getPatchProfileImage() {
        return this.getProfile_pic_url();
    }

    public int getIdProfile(){
        return this.id;
    }
}
