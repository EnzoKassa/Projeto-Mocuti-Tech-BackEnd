package com.api.mocuti.controller

import com.api.mocuti.entity.Endereco
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.EnderecoRepository

@RestController
@RequestMapping("/endereco")
class EnderecoJpaController(val repositorio: EnderecoRepository) {

    @PostMapping
    fun postEndereco(@RequestBody @Valid endereco: Endereco): ResponseEntity<Endereco> {
        val novoEndereco = repositorio.save(endereco)
        return if (novoEndereco == null) ResponseEntity.status(204).build()
        else ResponseEntity.status(201).body(novoEndereco)
    }

    @GetMapping
    fun getEndereco(): ResponseEntity<List<Endereco>> {
        val Endereco = repositorio.findAll()
        return if (Endereco.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(Endereco)
        }
    }

    @PutMapping("/{idEndereco}")
    fun putEndereco(
        @PathVariable idEndereco: Int,
        @RequestBody enderecoAtualizado: Endereco
    ):
            ResponseEntity<Endereco> {
        if (!repositorio.existsById(idEndereco)) {
            return ResponseEntity.status(404).build()
        }
        enderecoAtualizado.idEndereco = idEndereco
        val profissao = repositorio.save(enderecoAtualizado)
        return ResponseEntity.status(200).body(profissao)
    }

    @GetMapping("/usuario/{idUsuario}")
    fun getEnderecoDoUsuario(@PathVariable idUsuario: Int): ResponseEntity<Endereco> {
        val endereco = repositorio.findEnderecoByUsuarioId(idUsuario)
        return if (endereco == null) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(endereco)
        }
    }

    @GetMapping("/evento/{idEvento}")
    fun getEnderecoDoEvento(@PathVariable idEvento: Int): ResponseEntity<Endereco> {
        val endereco = repositorio.findEnderecoByEventoId(idEvento)
        return if (endereco == null) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(endereco)
        }
    }

}