package org.example.app;

import org.example.model.Objeto;
import org.example.service.OtimizadorHibridoService;
import org.example.service.impl.OtimizadorHibridoServiceImpl;

import java.util.List;

import static org.example.service.FileReader.getObjetos;
import static org.example.util.Values.LIMITE_MOCHILA;
import static org.example.util.Values.QUANTIDADE_ITENS;

public class ArtificialBeeColony_Hybrid_ED {

    public static void main(String[] args) {
        OtimizadorHibridoService otimizadorHibridoService = new OtimizadorHibridoServiceImpl();

        int tamanhoColonia = 50;
        int maxIteracoes = 100;
        int maxTentativas = 10;
        double probabilidadeAbandono = 0.3; /** */
        double fatorMutacaoED = 0.5; /** mutação para que o ED gere novas soluções */
        double taxaCrossoverED = 0.7; /** taxa de crossover do ED*/

        int numItens = QUANTIDADE_ITENS;
        int limiteMochila = LIMITE_MOCHILA.intValue();
        int[] pesosItens = new int[QUANTIDADE_ITENS];
        int[] valoresItens = new int[QUANTIDADE_ITENS];

        int somaValues = 0;
        int somaPesos = 0;

        /***/ long init = System.currentTimeMillis();
        List<Objeto> objetos = getObjetos();
        for (int i = 0; i < objetos.size(); i++) {
            pesosItens[i] = objetos.get(i).getPeso().intValue();
            valoresItens[i] = objetos.get(i).getValorTotal().intValue();
        }

//        ArtificialBeeColony_Hybrid_ED ABC_ED_mochila = new ArtificialBeeColony_Hybrid_ED(qtdAbelhas,
//                maxIteracoes, maxTentativas, probabilidadeAbandono, numItens, pesosItens, valoresItens, limiteMochila,
//                fatorMutacaoED, taxaCrossoverED);

        int[] solucoesOtimas = otimizadorHibridoService.otimizar(tamanhoColonia,
                maxIteracoes, maxTentativas, probabilidadeAbandono, numItens, pesosItens, valoresItens, limiteMochila,
                fatorMutacaoED, taxaCrossoverED);

//        int[] solucoesOtimas = ABC_ED_mochila.otimizar();
        /***/ long finish = System.currentTimeMillis();

        System.out.println("Best Solution:");


        for (int i = 0; i < numItens; i++) {
            if (solucoesOtimas[i] == 1) {
                System.out.println("Item " + i + ": Weight = " + pesosItens[i] + ", Value = " + valoresItens[i]);
                somaValues += valoresItens[i];
                somaPesos += pesosItens[i];
            }
        }
        System.out.println("Soma Total obtida \n--> Pesos: " + somaPesos + "\n --> Valores: " + somaValues);
        System.out.println("Tempo de processamento: " + (finish - init) + " milissegundos");

    }
}
