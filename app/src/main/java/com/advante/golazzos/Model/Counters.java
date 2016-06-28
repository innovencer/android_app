package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/17/2016.
 */
public class Counters extends Model {
    int marcadorTotalBets;
    int marcadorWonBets;
    int ganaPierdeTotalBets;
    int ganaPierdeWonBets;
    int diferenciaGolesTotalBets;
    int diferenciaGolesWonBets;
    int primerGolTotalBets;
    int primerGolWonBets;
    int numeroGolesTotalBets;
    int numeroGolesWonBets;
    int totalBets;
    int totalWonBets;

    public Counters() {
    }

    public Counters(int marcadorTotalBets, int marcadorWonBets, int ganaPierdeTotalBets, int ganaPierdeWonBets, int diferenciaGolesTotalBets, int diferenciaGolesWonBets, int primerGolTotalBets, int primerGolWonBets, int numeroGolesTotalBets, int numeroGolesWonBets, int totalBets, int totalWonBets) {
        this.marcadorTotalBets = marcadorTotalBets;
        this.marcadorWonBets = marcadorWonBets;
        this.ganaPierdeTotalBets = ganaPierdeTotalBets;
        this.ganaPierdeWonBets = ganaPierdeWonBets;
        this.diferenciaGolesTotalBets = diferenciaGolesTotalBets;
        this.diferenciaGolesWonBets = diferenciaGolesWonBets;
        this.primerGolTotalBets = primerGolTotalBets;
        this.primerGolWonBets = primerGolWonBets;
        this.numeroGolesTotalBets = numeroGolesTotalBets;
        this.numeroGolesWonBets = numeroGolesWonBets;
        this.totalBets = totalBets;
        this.totalWonBets = totalWonBets;
    }

    public int getMarcadorTotalBets() {
        return marcadorTotalBets;
    }

    public void setMarcadorTotalBets(int marcadorTotalBets) {
        this.marcadorTotalBets = marcadorTotalBets;
    }

    public int getMarcadorWonBets() {
        return marcadorWonBets;
    }

    public int getMarcardorLose_bets() {
        return marcadorTotalBets - marcadorWonBets;
    }

    public void setMarcadorWonBets(int marcadorWonBets) {
        this.marcadorWonBets = marcadorWonBets;
    }

    public int getGanaPierdeTotalBets() {
        return ganaPierdeTotalBets;
    }

    public void setGanaPierdeTotalBets(int ganaPierdeTotalBets) {
        this.ganaPierdeTotalBets = ganaPierdeTotalBets;
    }

    public int getGanaPierdeWonBets() {
        return ganaPierdeWonBets;
    }

    public int getGanaPierdeLose_bets() {
        return ganaPierdeTotalBets - ganaPierdeWonBets;
    }

    public void setGanaPierdeWonBets(int ganaPierdeWonBets) {
        this.ganaPierdeWonBets = ganaPierdeWonBets;
    }

    public int getTotalBets() {
        return totalBets;
    }

    public void setTotalBets(int totalBets) {
        this.totalBets = totalBets;
    }

    public int getTotalWonBets() {
        return totalWonBets;
    }

    public int getTotalLoseBets() {
        return totalBets - totalWonBets;
    }

    public void setTotalWonBets(int totalWonBets) {
        this.totalWonBets = totalWonBets;
    }

    public int getDiferenciaGolesTotalBets() {
        return diferenciaGolesTotalBets;
    }

    public void setDiferenciaGolesTotalBets(int diferenciaGolesTotalBets) {
        this.diferenciaGolesTotalBets = diferenciaGolesTotalBets;
    }

    public int getDiferenciaGolesWonBets() {
        return diferenciaGolesWonBets;
    }

    public void setDiferenciaGolesWonBets(int diferenciaGolesWonBets) {
        this.diferenciaGolesWonBets = diferenciaGolesWonBets;
    }

    public int getPrimerGolTotalBets() {
        return primerGolTotalBets;
    }

    public void setPrimerGolTotalBets(int primerGolTotalBets) {
        this.primerGolTotalBets = primerGolTotalBets;
    }

    public int getPrimerGolWonBets() {
        return primerGolWonBets;
    }

    public void setPrimerGolWonBets(int primerGolWonBets) {
        this.primerGolWonBets = primerGolWonBets;
    }

    public int getNumeroGolesTotalBets() {
        return numeroGolesTotalBets;
    }

    public void setNumeroGolesTotalBets(int numeroGolesTotalBets) {
        this.numeroGolesTotalBets = numeroGolesTotalBets;
    }

    public int getNumeroGolesWonBets() {
        return numeroGolesWonBets;
    }

    public void setNumeroGolesWonBets(int numeroGolesWonBets) {
        this.numeroGolesWonBets = numeroGolesWonBets;
    }
}
