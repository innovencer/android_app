package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 7/16/2016.
 */
public class Multipliers {
    String type;
    int subscribed;
    int non_subscribed;

    public Multipliers() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(int subscribed) {
        this.subscribed = subscribed;
    }

    public int getNon_subscribed() {
        return non_subscribed;
    }

    public void setNon_subscribed(int non_subscribed) {
        this.non_subscribed = non_subscribed;
    }
}
