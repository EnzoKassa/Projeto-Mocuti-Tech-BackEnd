package com.api.mocuti.controller

import com.api.mocuti.entity.StatusEvento
import com.api.mocuti.repository.StatusEventoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/status-eventos")
class StatusEventoJpaController(var repositorioStatusEvento: StatusEventoRepository) {

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