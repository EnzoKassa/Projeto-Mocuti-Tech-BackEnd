package com.api.mocuti.controller

import com.api.mocuti.entity.StatusInscricao
import com.api.mocuti.repository.StatusInscricaoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Status Inscrição", description = "Operações relacionadas aos status das inscrições")
@RequestMapping("/status-inscricoes")
class StatusInscricaoJpaController(var repositorioStatusInscricao: StatusInscricaoRepository) {

    @Operation(
        summary = "Listar todos os status de inscrições",
        description = "Retorna todos os status de inscrições cadastrados no sistema"
    )
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
