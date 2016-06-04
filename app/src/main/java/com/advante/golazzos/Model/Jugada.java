package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/27/2016.
 */
public class Jugada extends Model {
    int id;
    String textTime_ago;
    Equipo equipo1;
    Equipo equipo2;
    String label;
    String html_center_url;
    String trackable_type;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
