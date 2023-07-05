package org.example.service.impl;

import org.example.model.Bee;
import org.example.service.OtimizadorHibridoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OtimizadorHibridoServiceImpl implements OtimizadorHibridoService {

    private int colonySize;
    private int maxIterations;
    private int maxTrials;
    private double abandonProbability;
    private int numItems;
    private int[] weights;
    private int[] values;
    private int maxWeight;
    private double deMutationFactor;
    private double deCrossoverRate;
    private Random random;
    private List<Bee> coloniaDeAbelhas;

    @Override
    public int[] otimizar(int tamanhoColonia, int maxIteracoes, int maxTentativas, double probabilidadeAbandono,
                          int numItens, int[] pesosItens, int[] valoresItens, int limiteMochila,
                          double fatorMutacaoED, double taxaCrossoverED) {

        instanciar(tamanhoColonia, maxIteracoes, maxTentativas, probabilidadeAbandono,
        numItens, pesosItens, valoresItens, limiteMochila,
        fatorMutacaoED, taxaCrossoverED);

        inicializarColonia();
        Bee bestBee = getBestBee();

        for (int i = 0; i < maxIterations; i++) {
            for (Bee bee : coloniaDeAbelhas) {
                if (bee.getTrials() >= maxTrials) {
                    if (random.nextDouble() < abandonProbability) {
                        abandonarAbelha(bee);
                    } else {
                        explorarAbelha(bee);
                    }
                } else {
                    explorarAbelha(bee);
                }
            }

            Bee currentBestBee = getBestBee();
            if (currentBestBee.getFitness() > bestBee.getFitness()) {
                bestBee = currentBestBee;
            }
        }

        return bestBee.getSolution();
    }



    private void inicializarColonia() {
        for (int i = 0; i < colonySize; i++) {
            int[] solution = gerarSolucaoAleatoria();
            double fitness = avaliarSolucao(solution);
            Bee bee = new Bee(solution, fitness);
            coloniaDeAbelhas.add(bee);
        }
    }

    private int[] gerarSolucaoAleatoria() {
        int[] solution = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            solution[i] = random.nextInt(2);
        }
        return solution;
    }

    private void abandonarAbelha(Bee bee) {
        int[] randomSolution = gerarSolucaoAleatoria();
        double randomFitness = avaliarSolucao(randomSolution);
        bee.updateSolution(randomSolution, randomFitness);
        bee.resetTrials();
    }

    private void explorarAbelha(Bee bee) {
        int[] newSolution = gerarSolucaoED(bee.getSolution());
        double newFitness = avaliarSolucao(newSolution);

        if (newFitness > bee.getFitness()) {
            bee.updateSolution(newSolution, newFitness);
        } else {
            bee.incrementTrials();
        }
    }

    /**analisando a qualidade da solução gerada*/
    private double avaliarSolucao(int[] solution) {
        int pesoTotal = 0;
        int valortotal = 0;

        for (int i = 0; i < numItems; i++) {
            if (solution[i] == 1) {
                pesoTotal += weights[i];
                valortotal += values[i];
            }
        }

        if (pesoTotal > maxWeight) {
            valortotal = 0; // zera se exceder o peso máximo, indicando que a solução pode ser descartada
        }

        return valortotal;
    }

    private Bee getBestBee() {
        Bee bestBee = coloniaDeAbelhas.get(0);
        for (Bee bee : coloniaDeAbelhas) {
            if (bee.getFitness() > bestBee.getFitness()) {
                bestBee = bee;
            }
        }
        return bestBee;
    }

    /** atualização das soluções com Evolução Diferencial(ED)  */

    private int[] gerarSolucaoED(int[] currentSolution) {

        /** obtendo as soluções mutantes a partir da solução aleatória gerado pela colônia*/
        int[] randomBeeSolution1 = getAbelhaAleatoria();
        int[] randomBeeSolution2 = getAbelhaAleatoria();
        int[] randomBeeSolution3 = getAbelhaAleatoria();

        int[] newSolution = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            if (random.nextDouble() < deCrossoverRate) {
                newSolution[i] = (int) (randomBeeSolution1[i] + (deMutationFactor * (randomBeeSolution2[i] - randomBeeSolution3[i])));
                newSolution[i] = Math.max(0, Math.min(1, newSolution[i])); // Limitar entre 0 e 1
            } else {
                newSolution[i] = currentSolution[i];
            }
        }
        return newSolution;
    }

    private int[] getAbelhaAleatoria() {
        int indiceAbelhaRandom = random.nextInt(colonySize);
        return coloniaDeAbelhas.get(indiceAbelhaRandom).getSolution();
    }

    /**metodos auxilizares*/
    private void instanciar(int tamanhoColonia, int maxIteracoes, int maxTentativas, double probabilidadeAbandono,
                            int numItens, int[] pesosItens, int[] valoresItens, int limiteMochila,
                            double fatorMutacaoED, double taxaCrossoverED) {

        this.colonySize = tamanhoColonia;
        this.maxIterations = maxIteracoes;
        this.maxTrials = maxTentativas;
        this.abandonProbability = probabilidadeAbandono;
        this.numItems = numItens;
        this.weights = pesosItens;
        this.values = valoresItens;
        this.maxWeight = limiteMochila;
        this.deMutationFactor = fatorMutacaoED;
        this.deCrossoverRate = taxaCrossoverED;
        this.random = new Random();
        this.coloniaDeAbelhas = new ArrayList<>();
    }
}
