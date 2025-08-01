package com.api.mocuti.controller

import com.api.mocuti.entity.Categoria
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.CategoriaRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@Tag(name = "Categoria", description = "Operações relacionadas categoria dos eventos")

@RequestMapping("/categorias")
class CategoriaJpaController(val repositorio: CategoriaRepository) {

    @Operation(
        summary = "Listar todas as categorias",
        description = "Retorna uma lista com todas as categorias cadastradas"
    )
    @GetMapping
    fun get(): ResponseEntity<List<Categoria>> {
        val categorias = repositorio.findAll()

        return if (categorias.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(categorias)
        }
    }

    @Operation(
        summary = "Buscar categoria por ID",
        description = "Retorna a categoria referente ao ID fornecido"
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Categoria> {
        val categoria = repositorio.findById(id)

        return ResponseEntity.of(categoria)
    }

    @Operation(
        summary = "Deletar categoria por ID",
        description = "Remove a categoria referente ao ID fornecido"
    )
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {

        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @Operation(
        summary = "Criar uma nova categoria",
        description = "Adiciona uma nova categoria ao sistema"
    )
    @PostMapping
    fun post(@RequestBody @Valid novaCategoria: Categoria): ResponseEntity<Categoria> {
        val categoria = repositorio.save(novaCategoria)
        return ResponseEntity.status(201).body(categoria)
    }

    @Operation(
        summary = "Atualizar uma categoria",
        description = "Atualiza a categoria com o ID fornecido"
    )
    @PutMapping("/{id}")
    fun put(
        @PathVariable id: Int,
        @RequestBody categoriaAtualizada: Categoria
    ):
            ResponseEntity<Categoria> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }
        categoriaAtualizada.idCategoria = id
        val categoria = repositorio.save(categoriaAtualizada)
        return ResponseEntity.status(200).body(categoria)
    }
}