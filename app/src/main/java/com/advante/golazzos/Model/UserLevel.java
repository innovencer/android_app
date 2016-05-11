package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/7/2016.
 */
public class UserLevel extends Model{
    int hits_count;
    String logo_url;
    String name;
    int order;
    int points;

    public UserLevel(int hits_count, String logo_url, String name, int order, int points) {
        this.hits_count = hits_count;
        this.logo_url = logo_url;
        this.name = name;
        this.order = order;
        this.points = points;
    }

    public int getHits_count() {
        return hits_count;
    }

    public void setHits_count(int hits_count) {
        this.hits_count = hits_count;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
