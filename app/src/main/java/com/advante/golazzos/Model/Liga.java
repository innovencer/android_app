package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 4/10/2016.
 */
public class Liga {
    int data_factory_id;
    int id;
    String name;

    public Liga(int data_factory_id, int id, String name) {
        this.data_factory_id = data_factory_id;
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }
}
