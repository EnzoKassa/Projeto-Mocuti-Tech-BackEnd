package com.api.mocuti.controller

import com.api.mocuti.dto.PreferenciaRequest
import com.api.mocuti.entity.Preferencia
import com.api.mocuti.repository.PreferenciaRepository
import com.api.mocuti.service.PreferenciaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Preferências", description = "Operações relacionadas às preferências")
@RequestMapping("/preferencias")
class PreferenciaJpaController(
    var repositorioPreferencia: PreferenciaRepository,
    var preferenciaService: PreferenciaService
) {
    @PostMapping
    fun salvarPreferencia(@RequestBody req: PreferenciaRequest): ResponseEntity<Preferencia> {
        val preferencia = preferenciaService.salvar(req)
        return ResponseEntity.ok(preferencia)
    }

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