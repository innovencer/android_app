package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/7/2016.
 */

public class User extends Model {
    int id;
    String name;
    String email;
    Boolean paid_subscription;
    Double points;
    String profile_pic_url;
    UserLevel level;
    SoulTeam soul_team;

    public User() {
    }

    public User(int id, String name, String email, Boolean paid_subscription, Double points, String profile_pic_url, UserLevel level, SoulTeam soul_team) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.paid_subscription = paid_subscription;
        this.points = points;
        this.profile_pic_url = profile_pic_url;
        this.level = level;
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

    public Boolean getPaid_subscription() {
        return paid_subscription;
    }

    public void setPaid_subscription(Boolean paid_subscription) {
        this.paid_subscription = paid_subscription;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public UserLevel getLevel() {
        return level;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }

    public SoulTeam getSoul_team() {
        return soul_team;
    }

    public void setSoul_team(SoulTeam soul_team) {
        this.soul_team = soul_team;
    }
}
