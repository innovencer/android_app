package com.advante.golazzos.Model;

import java.util.ArrayList;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Ganador extends Model {
    String week_label;
    ArrayList<Ganador_Item> ranking_entries;

    public Ganador(String week_label, ArrayList<Ganador_Item> ranking_entries) {
        this.week_label = week_label;
        this.ranking_entries = ranking_entries;
    }

    public String getWeek_label() {
        return week_label;
    }

    public ArrayList<Ganador_Item> getRanking_entries() {
        return ranking_entries;
    }
}
