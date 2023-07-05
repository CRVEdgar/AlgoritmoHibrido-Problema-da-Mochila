package org.example.service;

import org.example.model.Objeto;
import org.example.model.ObjetosArmazenados;

import java.util.Comparator;
import java.util.List;

import static org.example.util.Values.LIMITE_MOCHILA;

public class GulosoService {

    /** @NOTE: ORDDENAÇÃO por peso permitirá que os objetos de menor peso estejam no inicio da lista,
     * dessa forma serão 'consumidos' primeiro pelo algoritmo guloso */
    public ObjetosArmazenados solverGulosoByPeso(List<Objeto> objetos) {
        ObjetosArmazenados objetosArmazenados = new ObjetosArmazenados();
        Double pesoAtual = 0.0;
        Double valorTotal = 0.0;

        /** Ordena por Peso - de modo que a ordem fique crescente*/
        objetos.sort( (p1, p2) -> p1.getPeso().compareTo(p2.getPeso()) );

        for (Objeto item : objetos) {
            if (pesoAtual + item.getPeso() <= LIMITE_MOCHILA) {
                objetosArmazenados.addObjeto(item);
                objetosArmazenados.addPeso(item.getPeso());
                objetosArmazenados.addValor(item.getValorTotal());

                pesoAtual += item.getPeso();
                valorTotal += item.getValorTotal();
            }

        }

        return objetosArmazenados;
    }

    /** @NOTE: ORDDENAÇÃO por valor permitirá que os objetos de maior valor estejam no inicio da lista,
     * dessa forma serão 'consumidos' primeiro pelo algoritmo guloso */
    public ObjetosArmazenados solverGulosoByValor(List<Objeto> objetos) {

        Comparator<Objeto> comparator = getOrderByValor();
        /** Ordena por Valor - invertendo a ordem do comparator, de modo que a ordem fique decrescente*/
        objetos.sort( comparator.reversed() );

        ObjetosArmazenados objetosArmazenados = new ObjetosArmazenados();
        Double pesoAtual = 0.0;
        Double valorTotal = 0.0;

        for (Objeto item : objetos) {
            if (pesoAtual + item.getPeso() <= LIMITE_MOCHILA) {
                objetosArmazenados.addObjeto(item);
                objetosArmazenados.addPeso(item.getPeso());
                objetosArmazenados.addValor(item.getValorTotal());

                pesoAtual += item.getPeso();
                valorTotal += item.getValorTotal();
            }

        }

        return objetosArmazenados;
    }

    private Comparator<Objeto> getOrderByValor() {
        /** ordena por Valor - ordem crescente*/
        return (p1, p2) -> p1.getValorTotal().compareTo(p2.getValorTotal());
    }
}
