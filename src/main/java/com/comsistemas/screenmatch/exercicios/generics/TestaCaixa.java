package com.comsistemas.screenmatch.exercicios.generics;

// Testaremos a criação de alguns objetos do tipo Caixa, cujo conteúdo poderá ser de qualquer tipo
public class TestaCaixa {
    public static void main(String[] args) {
        Caixa<String> caixaDeTexto = new Caixa();
        caixaDeTexto.setConteudo("Guardando texto na minha caixa");
        System.out.println(caixaDeTexto.somaConteudoNaCaixa("Mais uma linha"));

        Caixa<Integer> caixaDeIdade = new Caixa();
        caixaDeIdade.setConteudo(30);
        System.out.println(caixaDeIdade.somaConteudoNaCaixa(26));

        Caixa<Double> caixaDeValor = new Caixa();
        caixaDeValor.setConteudo(150.50);
        System.out.println(caixaDeValor.somaConteudoNaCaixa(350.50));
        System.out.println(caixaDeValor.somaConteudoNaCaixa("Texto"));
    }
}
