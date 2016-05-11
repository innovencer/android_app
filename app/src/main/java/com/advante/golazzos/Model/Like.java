package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/1/2016.
 */
public class Like extends Model{
    int id;
    String likeable_type;
    Boolean mine;

    public Like() {
    }

    public Like(int id, String likeable_type, Boolean mine) {
        this.id = id;
        this.likeable_type = likeable_type;
        this.mine = mine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLikeable_type() {
        return likeable_type;
    }

    public void setLikeable_type(String likeable_type) {
        this.likeable_type = likeable_type;
    }

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
    }
}
