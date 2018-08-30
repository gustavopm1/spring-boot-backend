package com.gpmoraes.desafio;

import com.gpmoraes.desafio.domain.*;
import com.gpmoraes.desafio.domain.enums.EstadoPagamento;
import com.gpmoraes.desafio.domain.enums.TipoCliente;
import com.gpmoraes.desafio.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@SpringBootApplication
public class DesafioApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


    }
}
