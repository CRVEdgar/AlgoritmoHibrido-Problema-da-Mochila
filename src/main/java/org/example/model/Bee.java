package org.example.model;

public class Bee {

    private int[] solucoes; /** solucoes 0 e 1*/
    private double valorObtido;
    private int trials;

    public Bee(int[] solution, double fitness) {
        this.solucoes = solution;
        this.valorObtido = fitness;
        this.trials = 0;
    }

    public int[] getSolucoesBin() {
        return solucoes;
    }

    public double getValorObtidoBee() {
        return valorObtido;
    }

    public int getTrials() {
        return trials;
    }

    public void updateSolution(int[] newSolution, double newFitness) {
        this.solucoes = newSolution;
        this.valorObtido = newFitness;
        this.trials = 0;
    }

    public void incrementTrials() {
        this.trials++;
    }

    public void resetTrials() {
        this.trials = 0;
    }
}
