package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/11/2016.
 */
public class SoulTeam extends Model {
    int id;
    String name;
    String image_path;

    public SoulTeam(String image_path, String name, int id) {
        this.image_path = image_path;
        this.name = name;
        this.id = id;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

}
