package com.advante.golazzos.Model;

import com.advante.golazzos.Helpers.General;

/**
 * Created by Ruben Flores on 4/22/2016.
 */
public class Partido extends Model{
    int id;
    String start_time_utc;
    Equipo local;
    Equipo visitante;
    Liga tournament;
    String html_center_url;
    int local_score = 0;
    int visitant_score = 0;

    public Partido(int id, String start_time_utc, Equipo local, Equipo visitante, Liga tournament, String html_center_url) {
        this.id = id;
        this.start_time_utc = start_time_utc;
        this.local = local;
        this.visitante = visitante;
        this.tournament = tournament;
        this.html_center_url = html_center_url;
    }

    public Partido() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_time_utc() {
        return start_time_utc;
    }

    public void setStart_time_utc(String start_time_utc) {
        this.start_time_utc = start_time_utc;
    }

    public Equipo getLocal() {
        return local;
    }

    public void setLocal(Equipo local) {
        this.local = local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public void setVisitante(Equipo visitante) {
        this.visitante = visitante;
    }

    public Liga getTournament() {
        return tournament;
    }

    public void setTournament(Liga tournament) {
        this.tournament = tournament;
    }

    public String getHtml_center_url() {
        return html_center_url;
    }

    public void setHtml_center_url(String html_center_url) {
        this.html_center_url = html_center_url;
    }

    public int getLocal_score() {
        return local_score;
    }

    public void setLocal_score(int local_score) {
        this.local_score = local_score;
    }

    public int getVisitant_score() {
        return visitant_score;
    }

    public void setVisitant_score(int visitant_score) {
        this.visitant_score = visitant_score;
    }
}
