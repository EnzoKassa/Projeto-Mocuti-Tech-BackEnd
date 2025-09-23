package com.api.mocuti.controller

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Categoria
import com.api.mocuti.service.CategoriaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Categoria", description = "Operações relacionadas categoria dos eventos")
@RequestMapping("/categorias")
class CategoriaJpaController(
    private val categoriaService: CategoriaService
) {

    @Operation(summary = "Listar todas as categorias")
    @GetMapping
    fun get(): ResponseEntity<List<Categoria>> {
        val categorias = categoriaService.listarTodos()
        return if (categorias.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(categorias)
    }

    @Operation(summary = "Buscar categoria por ID")
    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Categoria> {
        val categoria = categoriaService.buscarPorId(id)
        return if (categoria != null) ResponseEntity.ok(categoria)
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Deletar categoria por ID")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (categoriaService.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Criar uma nova categoria")
    @PostMapping
    fun post(@RequestBody @Valid novaCategoria: Categoria): ResponseEntity<Categoria> {
        val categoria = categoriaService.salvar(novaCategoria)
        return ResponseEntity.status(201).body(categoria)
    }

    @Operation(summary = "Atualizar uma categoria")
    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody categoriaAtualizada: Categoria): ResponseEntity<Categoria> {
        val categoria = categoriaService.atualizar(id, categoriaAtualizada)
        return if (categoria != null) ResponseEntity.ok(categoria)
        else ResponseEntity.notFound().build()
    }

    @Operation(summary = "Rank Categoria")
    @GetMapping("/view/ranking")
    fun getRanking(): ResponseEntity<List<RankCategoriaRequest>> {
        val ranking = categoriaService.getRanking()
        return ResponseEntity.ok(ranking)
    }
}