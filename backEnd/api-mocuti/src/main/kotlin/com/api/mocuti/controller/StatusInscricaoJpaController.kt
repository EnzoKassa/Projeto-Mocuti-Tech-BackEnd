package com.api.mocuti.controller

import com.api.mocuti.entity.StatusInscricao
import com.api.mocuti.repository.StatusInscricaoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/status-inscricoes")
class StatusInscricaoJpaController(var repositorioStatusInscricao: StatusInscricaoRepository) {
    @GetMapping
    fun get(): ResponseEntity<List<StatusInscricao>> {
        val statusInscricao = repositorioStatusInscricao.findAll()

        return if (statusInscricao.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(statusInscricao)
        }
    }
}