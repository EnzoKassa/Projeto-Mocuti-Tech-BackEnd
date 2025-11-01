package com.api.mocuti

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration // Anotação para indicar que esta classe é uma configuração do Spring

/**
 *  Cria a classe WebConfig que implementa (:) WebMvcConfigurer para poder sobrescrever
 *  métodos de configuração (no caso, addCorsMappings).
 *      WebMvcConfigurer → interface que permite configurar o Spring MVC.
 */
class WebConfig : WebMvcConfigurer {
    /**
     * Sobrescreve o método que configura as regras de CORS para toda a aplicação.
     * O parâmetro registry: É onde você adiciona e configura as permissões.
     * CorsRegistry → objeto que define as regras de CORS.
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // libera para todas as rotas
            .allowedOrigins("http://localhost:5173") // endereço do frontend Vite
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Lista os métodos HTTP permitidos.
            .allowedHeaders("*") // Permite qualquer cabeçalho HTTP.
            .allowCredentials(true) // se precisar enviar cookies/autenticação
            .maxAge(3600)
    }
}
