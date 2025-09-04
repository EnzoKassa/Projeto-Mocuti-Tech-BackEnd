package com.api.mocuti.controller

import com.api.mocuti.entity.Cargo
import com.api.mocuti.repository.CargoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Cargo", description = "Operações relacionadas cargo de funcionário")
@RequestMapping("/cargos")
class CargoJpaController(var repositorioCargo: CargoRepository) {

    @Operation(
        summary = "Listar todos os cargo",
        description = "Retorna uma lista com todos os cargos cadastrados"
    )
    @GetMapping
    fun getCargo(): ResponseEntity<List<Cargo>> {
        val cargos = repositorioCargo.findAll()

        return if (cargos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(cargos)
        }
    }
}