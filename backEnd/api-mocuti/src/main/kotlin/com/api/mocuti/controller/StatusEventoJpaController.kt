package com.api.mocuti.controller

import com.api.mocuti.entity.StatusEvento
import com.api.mocuti.repository.StatusEventoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Status Evento", description = "Operações relacionadas aos status dos eventos")
@RequestMapping("/status-eventos")
class StatusEventoJpaController(var repositorioStatusEvento: StatusEventoRepository) {

    @Operation(
        summary = "Listar todos os status de eventos",
        description = "Retorna todos os status de eventos cadastrados no sistema"
    )
    @GetMapping
    fun get(): ResponseEntity<List<StatusEvento>> {
        val statusEvento = repositorioStatusEvento.findAll()

        return if (statusEvento.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(statusEvento)
        }
    }
}
