package com.api.mocuti

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiMocutiApplication

fun main(args: Array<String>) {
	// Carrega o .env da raiz do projeto
	val dotenv = Dotenv.configure()
		.ignoreIfMissing() // não dá erro se não existir (ex: produção)
		.load()

	// Joga as variáveis do .env como System properties
	dotenv.entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}

	runApplication<ApiMocutiApplication>(*args)
}
