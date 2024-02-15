package com.comsistemas.screenmatch.explicacoes.lambdastream;

import java.util.Arrays;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        // Tem esse formato:
        //  (parametro) -> expressao
        // Muito útil usar lambda com stream
        // Stream é um fluxo de dados e dá o poder de fazer operações encadeadas,
        // onde cada operação pode gerar outro fluxo de dados encadeado
        // Exemplo:
        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");

        // aqui nesse stream eu pedi para ordenar os nomes e depois imprimir
        // observar que .sorted() é considerado uma operação intermediária e
        // .forEach é considerado uma operação final
        nomes.stream()
                .sorted()
                .forEach(System.out::println);

        System.out.println("--------------------");
        // já este exemplo, depois de ordenado, imprime os 3 primeiros
        nomes.stream()
                .sorted() // operação intermediária
                .limit(3)  // operação intermediária
                .forEach(System.out::println);  // operação final

        System.out.println("--------------------");
        // aumentando os encadeamentos, nesse exemplo, depois de pegar os 3 primeiros nomes
        // que foram ordenados, eu filtro pegando apenas o(s) nomes(s) que começam com a
        // letra 'N' (no caso 'Nico'), depois transformo o nome em maísculo para no final
        // (operação final), imprimir o resultado
        // Observar que a operação final não se limita em usar apenas o forEach
        nomes.stream()
                .sorted()
                .limit(3)
                .filter(n -> n.startsWith("N"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);

    }

}
