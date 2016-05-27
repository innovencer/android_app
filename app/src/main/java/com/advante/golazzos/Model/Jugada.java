package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/27/2016.
 */
public class Jugada extends Model {
    int id;
    String textTime_ago;
    Equipo equipo1;
    Equipo equipo2;
    int type = 1;

    public Jugada() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextTime_ago() {
        return textTime_ago;
    }

    public void setTextTime_ago(String textTime_ago) {
        this.textTime_ago = textTime_ago;
    }

    public Equipo getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
