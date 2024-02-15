package com.comsistemas.screenmatch;

import com.comsistemas.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// A princípio iremos fazer uma aplicação de linha de comando, então eu
// preciso implementar a interface de linha de comando, que nos obriga a
// implementar o método obrigatório 'run'
@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
