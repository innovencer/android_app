package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/10/2016.
 */
public class Equipo {
    int id;
    int data_factory_id;
    String name;
    String complete_name;
    String country_name;
    String image_path;
    String type;
    String initials;

    public Equipo() {
    }

    public Equipo(int id, String name,String image_path) {
        this.id = id;
        this.name = name;
        this.image_path = image_path;
    }

    public Equipo(int id, String name, String complete_name, String country_name, String image_path, String type, String initials,int data_factory_id) {
        this.id = id;
        this.name = name;
        this.complete_name = complete_name;
        this.country_name = country_name;
        this.image_path = image_path;
        this.type = type;
        this.initials = initials;
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

    public String getComplete_name() {
        return complete_name;
    }

    public void setComplete_name(String complete_name) {
        this.complete_name = complete_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public int getData_factory_id() {
        return data_factory_id;
    }

    public void setData_factory_id(int data_factory_id) {
        this.data_factory_id = data_factory_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
