package com.comsistemas.screenmatch.exercicios.generics;

// Classe genérica que armazena um valor de um tipo desconhecido
// Poderemos criar um objeto do tipo Caixa e armazenar qualquer tipo de valor no mesmo
public class Caixa<T> {
    private T conteudo;

    public T getConteudo() {
        return conteudo;
    }

    public void setConteudo(T conteudo) {
        this.conteudo = conteudo;
    }

    public <T> T somaConteudoNaCaixa(T valor) {
        if (this.conteudo instanceof Integer c && valor instanceof Integer i) {
            Integer resultado = c + i;
            return (T) resultado;
        } else if (this.conteudo instanceof Double c && valor instanceof Double d) {
            Double resultado = c + d;
            return (T) resultado;
        } else if (this.conteudo instanceof String c && valor instanceof String s) {
            String resultado = c + "\n" + s;
            return (T) resultado;
        }

        return null;
    }
}
