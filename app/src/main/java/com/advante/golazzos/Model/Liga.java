package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/10/2016.
 */
public class Liga {
    int data_factory_id;
    int id;
    String name;
    String logo;

    public Liga(int data_factory_id, int id, String name, String logo) {
        this.data_factory_id = data_factory_id;
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public int getData_factory_id() {
        return data_factory_id;
    }

    public void setData_factory_id(int data_factory_id) {
        this.data_factory_id = data_factory_id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return name;
    }
}
