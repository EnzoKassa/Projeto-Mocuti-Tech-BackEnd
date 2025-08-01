package com.api.mocuti.controller

import com.api.mocuti.entity.PublicoAlvo
import com.api.mocuti.repository.PublicoAlvoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Público Alvo", description = "Operações relacionadas aos públicos alvos")
@RequestMapping("/publicos-alvos")
class PublicoAlvoJpaController(var repositorioPublicoAlvo: PublicoAlvoRepository) {

    @Operation(
        summary = "Listar todos os públicos alvos",
        description = "Retorna todos os públicos alvos cadastrados no sistema"
    )
    @GetMapping
    fun get(): ResponseEntity<List<PublicoAlvo>> {
        val publicoAlvo = repositorioPublicoAlvo.findAll()

        return if (publicoAlvo.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(publicoAlvo)
        }
    }
}
