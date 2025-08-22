package com.api.mocuti.controller

import com.api.mocuti.entity.VisaoGeralUsuariosView
import com.api.mocuti.repository.VisaoGeralUsuariosRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/view/visao-geral-usuarios")
class VisaoGeralUsuariosController(
    private val visaoGeralUsuariosRepository: VisaoGeralUsuariosRepository
) {

    @Operation(
        summary = "Listar todos os tipos de Usuários e se estão ativos ou inativos",
        description = "Retorna uma lista todos os tipos de Usuários e se estão ativos ou inativos"
    )
    @GetMapping
    fun get(): ResponseEntity<List<VisaoGeralUsuariosView>> {
        val canais = visaoGeralUsuariosRepository.findAll()

        return if (canais.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(canais)
        }
    }
}
