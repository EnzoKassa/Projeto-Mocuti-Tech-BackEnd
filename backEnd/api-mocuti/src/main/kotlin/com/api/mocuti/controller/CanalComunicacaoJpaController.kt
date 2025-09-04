package com.api.mocuti.controller

import com.api.mocuti.entity.CanalComunicacao
import com.api.mocuti.repository.CanalComunicacaoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Canal de Comunicação", description = "Operações relacionadas ao canal de comunicação")
@RequestMapping("/canal-comunicacao")
class CanalComunicacaoJpaController(var repositorioCanalComunicacao: CanalComunicacaoRepository) {

    @Operation(
        summary = "Listar todos os canais de comunicação",
        description = "Retorna uma lista com todos os canais de comunicação cadastrados"
    )
    @GetMapping
    fun getCanalComunicacao(): ResponseEntity<List<CanalComunicacao>> {
        val canais = repositorioCanalComunicacao.findAll()

        return if (canais.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(canais)
        }
    }
}