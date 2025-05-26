package com.api.mocuti.controller

import com.api.mocuti.entity.PublicoAlvo
import com.api.mocuti.repository.PublicoAlvoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/publicos-alvos")
class PublicoAlvoJpaController (var repositorioPublicoAlvo: PublicoAlvoRepository) {

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