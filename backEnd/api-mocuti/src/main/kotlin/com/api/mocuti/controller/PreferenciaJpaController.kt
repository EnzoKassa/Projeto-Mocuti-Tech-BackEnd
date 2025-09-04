package com.api.mocuti.controller

import com.api.mocuti.entity.Preferencia
import com.api.mocuti.repository.PreferenciaRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Preferências", description = "Operações relacionadas às preferências")
@RequestMapping("/preferencias")
class PreferenciaJpaController(var repositorioPreferencia: PreferenciaRepository) {

    @Operation(
        summary = "Listar todas as preferências",
        description = "Retorna todas as preferências cadastradas no sistema"
    )
    @GetMapping
    fun getPreferencia(): ResponseEntity<List<Preferencia>> {
        val preferencias = repositorioPreferencia.findAll()

        return if (preferencias.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(preferencias)
        }
    }
}