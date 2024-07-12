package com.aluracursos.Challenge2Cristhian;

import com.aluracursos.Challenge2Cristhian.principal.Principal;
import com.aluracursos.Challenge2Cristhian.repository.AutorRepository;
import com.aluracursos.Challenge2Cristhian.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Challenge2CristhianApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(Challenge2CristhianApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal menu = new Principal(libroRepository, autorRepository);
		menu.MuestraElMenu();
	}
}
