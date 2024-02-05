package com.comsistemas.screenmatch;

import com.comsistemas.screenmatch.model.DadosSerie;
import com.comsistemas.screenmatch.service.ConsumoApi;
import com.comsistemas.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// A princípio iremos fazer uma aplicação de linha de comando, então eu
// preciso implementar a interface de linha de comando e então eu preciso
// implementgar o método obrigatório 'run'
@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		// Consumo da Api
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		//json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);
		// Instancio o conversor
		ConverteDados conversor = new ConverteDados();
		// Quero representar o resultado como DadosSerie
		// Transformando o json em DadosSerie (desserializando)
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

	}
}
