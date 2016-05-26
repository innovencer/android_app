package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/3/2016.
 */
public class Ganador_Item extends  Model{
    String patchProfileImage;
    String patchSoulTeam;
    String name;
    String souldTeamName;
    int level;
    int premios;
    int idProfile;
    int type = 1;
    int rank;

    public Ganador_Item() {
    }

    public String getPatchProfileImage() {
        return patchProfileImage;
    }

    public void setPatchProfileImage(String patchProfileImage) {
        this.patchProfileImage = patchProfileImage;
    }

    public String getPatchSoulTeam() {
        return patchSoulTeam;
    }

    public void setPatchSoulTeam(String patchSoulTeam) {
        this.patchSoulTeam = patchSoulTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSouldTeamName() {
        return souldTeamName;
    }

    public void setSouldTeamName(String souldTeamName) {
        this.souldTeamName = souldTeamName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPremios() {
        return premios;
    }

    public void setPremios(int premios) {
        this.premios = premios;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
