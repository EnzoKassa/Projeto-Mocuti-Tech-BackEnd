package com.api.mocuti.controller

import com.api.mocuti.entity.CanalComunicacao
import com.api.mocuti.repository.CanalComunicacaoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/canal-comunicacao")
class CanalComunicacaoJpaController (var repositorioCanalComunicacao: CanalComunicacaoRepository) {
    @GetMapping
    fun get(): ResponseEntity<List<CanalComunicacao>> {
        val canais = repositorioCanalComunicacao.findAll()

        return if (canais.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(canais)
        }
    }
}