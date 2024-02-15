package com.comsistemas.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {
    // agora é com a lib jackson, mas no Gson utilizava-se o toJson e FromJson
    private ObjectMapper mapper = new ObjectMapper();


    // Feito dessa forma fica mais genérico, pois a obtenção dos dados podem ir
    // para DadosSerie, DadosTemporada e DadosEpisodio, dependendo da ocasião.
    // Para isso a classe ConverteDados implementou a interface IConverteDados
    // com o método obterDados que sobrescrevemos aqui. Usando generics.
    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe); // essa linha converte o json para a classe
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
