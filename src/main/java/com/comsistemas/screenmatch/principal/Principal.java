package com.comsistemas.screenmatch.principal;

import com.comsistemas.screenmatch.model.DadosEpisodio;
import com.comsistemas.screenmatch.model.DadosSerie;
import com.comsistemas.screenmatch.model.DadosTemporada;
import com.comsistemas.screenmatch.model.Episodio;
import com.comsistemas.screenmatch.service.ConsumoApi;
import com.comsistemas.screenmatch.service.ConverteDados;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    // Definição de constantes, deve seguir essa prática e sintaxe:
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        // Consumo da Api
        // Obtém dados da Série
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
//        Esse exemplo abaixo pega um link que apresentará a imagem de um café aleatório
//        json = consumo.obterDados("https://coffee.alexflipnote.dev/random.json");
        // Continuando, depois do consumo da Api com o resultado em json, agora é preciso desserializar, ou seja,
        // é preciso transformar o json em objeto java.
        // Então representaremos o json como DadosSerie, que é um record com os campos:
        // 'titulo', 'totalTemporadas' e 'avaliacao' (só quero pegar esses campos).
        System.out.println("Json: " + json);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class); // trará titulo, totalTemporadas e avaliacao
        System.out.println("Impressão dos dados da série: " + dadosSerie);

   		// Obtém dados de cada temporada
		List<DadosTemporada> temporadas = new ArrayList<>(); // numero da temporada e lista dos episodios com os campos titulo, numero, avaliacao e dataLancamento

		for (int i = 1; i <= dadosSerie.totalTemporadas() ; i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		//for (DadosTemporada temporada : temporadas) {
		//	System.out.println(temporada);
		//}
		// o for/each acima pode ser substituído por:
		// lista os numeros das temporadas e sua lista de episodios
        System.out.println("Listagem das temporadas que foram adicionadas na lista temporadas:");
        // essa é a forma resumida do lambda, chamada de method reference (quando usa ::)
        // sempre quando o lambda tem um único parâmetro que chama uma única função, podemos substituir pelo method reference
        temporadas.forEach(System.out::println); // seria o mesmo que lambda: temporadas.forEach(t -> System.out.println(t))

        //Imprimir todos os títulos dos episódios de todas as temporadas
//        for(int i = 0; i < dadosSerie.totalTemporadas(); i++) { //dadosSerie tem titulo da serie, totalTemporadas e avaliacao
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        // Usando lambdas, substitui os 2 for acima e faz a mesma coisa
        // t -> itera títulos, e -> itera episódios
        System.out.println("Impressão de todos os títulos de todos os episódios:");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        // O USO DOS STREAMS AGORA IRÁ SUBSTITUIR TER QUE PERCORRER MAIS DE UMA LISTA UMA DENTRO DA OUTRA COMO ACIMA
        // QUERO PEGAR A LISTA DE TEMPORADAS E TRANSFORMAR NUMA LISTA DE EPISODIOS
        // Pegar os 5 melhores episódios de todas as temporadas
        // Transformar numa única lista somente dos dados de todos os episódios de todas as temporadas
        // O map é um recurso que transforma os dados
        // flatMap usa uma lista dentro de outra (transforma em zero ou em muitos)
        // Esse List vai armazenar todos os episódios de todas as temporadas
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList()); // aqui gera uma lista não imutável
                //.toList(); // se colocasse somente .toList(), geraria uma lista imutável
        // testar a imutabilidade assim:
        //dadosEpisodios.add(new DadosEpisodio("teste", 3, "10", "2020-01-01")); // daria erro
        System.out.println("Transformação das temporadas em dadosEpisodios:");
        dadosEpisodios.forEach(System.out::println);

        System.out.println("\nTop 10 episódios:");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))     // ignora os episódios cuja avalição seja = "N/A"
                // o peek é pra quando rodar, mostrar o que o código está fazendo (é uma "olhadinha")
                //.peek(e -> System.out.println("Primeiro filtro (N/A): " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())  // faz um sort da maior avaliação para a menor
                //.peek(e -> System.out.println("Ordenação: " + e))
                .limit(10)
                //.peek(e -> System.out.println("Limite: " + e))
                .map(e -> e.titulo().toUpperCase())// pega somente os 5 com melhores avaliações (5 primeiros)
                //.peek(e -> System.out.println("Mapeamento: " + e))
                .forEach(System.out::println);                                      // imprime a lista

        // Listando agora as temporadas e episódios
        System.out.println("----------------------------------------");
        List<Episodio> episodios = temporadas.stream()  // inicializa um fluxo de temporadas e atribui-o a uma lista de objetos episodios
                //.flatMap transforma um elemento em zero ou mais elementos (um para muitos). Transformando temporadas em episódios
                .flatMap(t -> t.episodios().stream()    // Transforma cada elemento do fluxo em um fluxo de episódios usando o método episodios() e, em seguida, achatando-o em um único fluxo
                        // .map transforma um elemento em outro (uma pra um). Transforma os dados episódios em novos episódios
                        .map(d -> new Episodio(t.numero(), d)) //Cada elemento no fluxo é transformado em um objeto ‘Episodio’ com os parâmetros (t, numero, d) usando a operação de mapeamento
                                                               // então os parâmetros são numero do episodio e os dados do episodio
                ).collect(Collectors.toList());
        System.out.println("\nLista transformada das temporadas em episódios: ");
        episodios.forEach(System.out::println);

        // Filtrar por um trecho do título do episódio.
        // A primeira ocorrência desse trecho encontrado será considerado.
        // Se o trecho do episódio for encontrado, retornará de qual temporada ele pertence.
//        System.out.println("\nDigite um trecho do título do episódio: ");
//        var trechoTitulo = leitura.nextLine();
//        // Optional é um objeto conteiner que pode ou não conter um valor não nulo
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBuscado.isPresent()) {
//            System.out.println("Episódio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episódio não encontrado! ");
//        }


//        // Agora eu quero imprimir os episódios a partir de uma determinada data e
//        // com a data formatada no padrão brasileiro: dd/mm/aaaa
//        System.out.println("\nA partir de qual ano você quer ver os episódios?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));
//

        // Agrupamento de dados usando Map para pegar número temporada e média das avaliações
        System.out.println("\nAgrupando número da temporada e a sua média com o Map: ");
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println("\nAvaliações por temporadas: " + avaliacoesPorTemporada);

        // Estatísticas relevantes com a classe DoubleSummaryStatistics
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("\nEstatísticas gerais das avaliações: " + est);

        // Se eu quiser selecionar as estatísticas eu faria assim:
        DoubleSummaryStatistics est2 = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("\nSelecionando algumas estatísticas: " );
        System.out.println("Média: " + est2.getAverage() + " Melhor episódio: " + est2.getMax() +
                " Pior episódio: " + est2.getMin() + " Quantidade episódios: " + est2.getCount());

    }
}
