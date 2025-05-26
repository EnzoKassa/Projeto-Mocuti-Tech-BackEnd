package com.api.mocuti.controller

import com.api.mocuti.entity.Participacao
import com.api.mocuti.repository.ParticipacaoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/particopacoes")
class ParticipacaoJpaController(var repositorioParticipacao: ParticipacaoRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Participacao>> {
        val participacoes = repositorioParticipacao.findAll()

        return if (participacoes.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(participacoes)
        }
    }
}