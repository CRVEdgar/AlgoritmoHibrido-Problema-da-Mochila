package org.example.service;

public interface OtimizadorHibridoService {

    int[] otimizar(int tamanhoColonia, int maxIteracoes, int maxTentativas, double probabilidadeAbandono,
                   int numItens, int[] pesosItens, int[] valoresItens, int limiteMochila,
                   double fatorMutacaoED, double taxaCrossoverED);
}
