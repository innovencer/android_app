package com.advante.golazzos.Model;

import java.util.ArrayList;

/**
 * Created by Ruben Flores on 4/24/2016.
 */
public class Post extends  Model {
    int id;
    String label;
    String time_ago;
    Owner owner;
    ArrayList<Like> likes;
    int likedByMe = -1;
    String html_center_url = "";
    String trackable_type = "";
    String image_url = "";
    public Post() {

    }

    public Post(int id, String label, String time_ago, Owner owner, ArrayList<Like> likes) {
        this.id = id;
        this.label = label;
        this.time_ago = time_ago;
        this.owner = owner;
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    public int getLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(int likedByMe) {
        this.likedByMe = likedByMe;
    }

    public String getHtml_center_url() {
        return html_center_url;
    }

    public void setHtml_center_url(String html_center_url) {
        this.html_center_url = html_center_url;
    }

    public String getTrackable_type() {
        return trackable_type;
    }

    public void setTrackable_type(String trackable_type) {
        this.trackable_type = trackable_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
