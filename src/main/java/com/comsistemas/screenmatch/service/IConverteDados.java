package com.comsistemas.screenmatch.service;

public interface IConverteDados {
    // Generics: <T> T, vai retornar algum tipo que vai ser genérico
    // recebe um json e recebe uma classe
    <T> T obterDados(String json, Class<T> classe);
}
