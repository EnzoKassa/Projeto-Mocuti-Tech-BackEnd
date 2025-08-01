package com.api.mocuti.controller

import com.api.mocuti.entity.Endereco
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.EnderecoRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@Tag(name = "Endereço", description = "Operações relacionadas a endereços de usuários e eventos")
@RequestMapping("/endereco")
class EnderecoJpaController(val repositorio: EnderecoRepository) {

    @Operation(
        summary = "Cadastrar um novo endereço",
        description = "Cria e persiste um novo endereço no banco de dados"
    )
    @PostMapping
    fun postEndereco(@RequestBody @Valid endereco: Endereco): ResponseEntity<Endereco> {
        val novoEndereco = repositorio.save(endereco)
        return ResponseEntity.status(201).body(novoEndereco)
    }

    @Operation(
        summary = "Listar todos os endereços",
        description = "Retorna todos os endereços cadastrados; retorna 204 se estiver vazio"
    )
    @GetMapping
    fun getEndereco(): ResponseEntity<List<Endereco>> {
        val endereco = repositorio.findAll()
        return if (endereco.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(endereco)
        }
    }

    @Operation(
        summary = "Atualizar um endereço",
        description = "Atualiza os dados de um endereço existente a partir do ID"
    )
    @PutMapping("/{idEndereco}")
    fun putEndereco(
        @PathVariable idEndereco: Int,
        @RequestBody enderecoAtualizado: Endereco
    ): ResponseEntity<Endereco> {
        if (!repositorio.existsById(idEndereco)) {
            return ResponseEntity.status(404).build()
        }
        enderecoAtualizado.idEndereco = idEndereco
        val endereco = repositorio.save(enderecoAtualizado)
        return ResponseEntity.status(200).body(endereco)
    }

    @Operation(
        summary = "Buscar endereço por ID de usuário",
        description = "Retorna o endereço vinculado ao usuário informado"
    )
    @GetMapping("/usuario/{idUsuario}")
    fun getEnderecoDoUsuario(@PathVariable idUsuario: Int): ResponseEntity<Endereco> {
        val endereco = repositorio.findByUsuarioId(idUsuario)
        return if (endereco == null) {
            ResponseEntity.status(404).build()
        } else {
            ResponseEntity.status(200).body(endereco)
        }
    }

    @Operation(
        summary = "Buscar endereço por ID de evento",
        description = "Retorna o endereço vinculado ao evento informado"
    )
    @GetMapping("/evento/{idEvento}")
    fun getEnderecoDoEvento(@PathVariable idEvento: Int): ResponseEntity<Endereco> {
        val endereco = repositorio.findEnderecoByEventoId(idEvento)
        return if (endereco == null) {
            ResponseEntity.status(404).build()
        } else {
            ResponseEntity.status(200).body(endereco)
        }
    }
}