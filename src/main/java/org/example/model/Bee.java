package org.example.model;

public class Bee {

    private int[] solution; /** solucao */
    private double fitness;
    private int trials;

    public Bee(int[] solution, double fitness) {
        this.solution = solution;
        this.fitness = fitness;
        this.trials = 0;
    }

    public int[] getSolution() {
        return solution;
    }

    public double getFitness() {
        return fitness;
    }

    public int getTrials() {
        return trials;
    }

    public void updateSolution(int[] newSolution, double newFitness) {
        this.solution = newSolution;
        this.fitness = newFitness;
        this.trials = 0;
    }

    public void incrementTrials() {
        this.trials++;
    }

    public void resetTrials() {
        this.trials = 0;
    }
}
