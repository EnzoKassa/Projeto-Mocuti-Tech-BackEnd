package com.api.mocuti.controller

import com.api.mocuti.entity.Cargo
import com.api.mocuti.repository.CargoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cargos")
class CargoJpaController(var repositorioCargo: CargoRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Cargo>> {
        val cargos = repositorioCargo.findAll()

        return if (cargos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(cargos)
        }
    }
}