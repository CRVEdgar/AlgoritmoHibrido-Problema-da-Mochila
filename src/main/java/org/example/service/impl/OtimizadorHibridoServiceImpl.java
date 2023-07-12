package org.example.service.impl;

import org.example.model.Bee;
import org.example.service.OtimizadorHibridoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OtimizadorHibridoServiceImpl implements OtimizadorHibridoService {

    private int tamColonia;
    private int totalIteracoes;
    private int limiteTentativas;
    private double probabilidadeDeAbandono;
    private int qtdInstancias;
    private int[] pesos;
    private int[] valores;
    private int capacidadeMochila;
    private double fatorDeMutacaoDE;
    private double taxaCruzamento;
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
        Bee melhorAbelha = getMelhorAbelha();

        /** ITERA SOBRE OS ITENS OTIMOS*/
        for (int i = 0; i < totalIteracoes; i++) {
            for (Bee bee : coloniaDeAbelhas) {
                //System.out.println("trials: " + bee.getTrials());
                if (bee.getTrials() >= limiteTentativas) {
                    if (random.nextDouble() < probabilidadeDeAbandono) {
                        abandonarAbelha(bee);
                    } else {
                        explorarAbelha(bee);
                    }
                } else {
                    explorarAbelha(bee);
                }
            }

            Bee melhorAbelhaAtual = getMelhorAbelha();
            //System.out.println("FITNESS: " + melhorAbelhaAtual.getValorObtidoBee() + "||" + " fitness anterior: " + melhorAbelha.getValorObtidoBee());
            if (melhorAbelhaAtual.getValorObtidoBee() > melhorAbelha.getValorObtidoBee()) {
                melhorAbelha = melhorAbelhaAtual;
            }
//            if(!(i>=33)){
//                System.out.println("SOLUTION: " + melhorAbelha.getSolution()[i]);
//            }
        }

        return melhorAbelha.getSolucoesBin();
    }



    private void inicializarColonia() {
        for (int i = 0; i < tamColonia; i++) {
            int[] solution = gerarSolucaoAleatoria();
            double fitness = avaliarSolucao(solution);
            Bee bee = new Bee(solution, fitness);
            coloniaDeAbelhas.add(bee);
        }
    }

    private int[] gerarSolucaoAleatoria() {
        int[] solution = new int[qtdInstancias];
        for (int i = 0; i < qtdInstancias; i++) {
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
        int[] newSolution = gerarSolucaoED(bee.getSolucoesBin());
        double newFitness = avaliarSolucao(newSolution);

        if (newFitness > bee.getValorObtidoBee()) {
            bee.updateSolution(newSolution, newFitness);
        } else {
            bee.incrementTrials();
        }
    }

    /**analisando a qualidade da solução gerada*/
    private double avaliarSolucao(int[] solution) {
        int pesoTotal = 0;
        int valortotal = 0;

        for (int i = 0; i < qtdInstancias; i++) {
            if (solution[i] == 1) {
                pesoTotal += pesos[i];
                valortotal += valores[i];
            }
        }

        if (pesoTotal > capacidadeMochila) {
            valortotal = 0; // zera se exceder o peso máximo, indicando que a solução pode ser descartada
        }

        return valortotal;
    }

    private Bee getMelhorAbelha() {
        Bee bestBee = coloniaDeAbelhas.get(0);
        for (Bee bee : coloniaDeAbelhas) {
            if (bee.getValorObtidoBee() > bestBee.getValorObtidoBee()) {
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

        int[] newSolution = new int[qtdInstancias];
        for (int i = 0; i < qtdInstancias; i++) {
            if (random.nextDouble() < taxaCruzamento) {
                newSolution[i] = (int) (randomBeeSolution1[i] + (fatorDeMutacaoDE * (randomBeeSolution2[i] - randomBeeSolution3[i])));
                newSolution[i] = Math.max(0, Math.min(1, newSolution[i])); //
            } else {
                newSolution[i] = currentSolution[i];
            }
        }
        return newSolution;
    }

    private int[] getAbelhaAleatoria() {
        int indiceAbelhaRandom = random.nextInt(tamColonia);
        return coloniaDeAbelhas.get(indiceAbelhaRandom).getSolucoesBin();
    }

    /**metodos auxilizares*/
    private void instanciar(int tamanhoColonia, int maxIteracoes, int maxTentativas, double probabilidadeAbandono,
                            int numItens, int[] pesosItens, int[] valoresItens, int limiteMochila,
                            double fatorMutacaoED, double taxaCrossoverED) {

        this.tamColonia = tamanhoColonia;
        this.totalIteracoes = maxIteracoes;
        this.limiteTentativas = maxTentativas;
        this.probabilidadeDeAbandono = probabilidadeAbandono;
        this.qtdInstancias = numItens;
        this.pesos = pesosItens;
        this.valores = valoresItens;
        this.capacidadeMochila = limiteMochila;
        this.fatorDeMutacaoDE = fatorMutacaoED;
        this.taxaCruzamento = taxaCrossoverED;
        this.random = new Random();
        this.coloniaDeAbelhas = new ArrayList<>();
    }
}
