package sptech.projeto05.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.projeto05.entity.Categoria


class CategoriaController {


        val categorias = mutableListOf(
            Categoria(1, "Palestra", "Palestras tematicas com power point"),
            Categoria(2, "Doação", "Evento de doação de roupas e/ou brinquedos"),
            Categoria(3, "Carnaval", "Comemoração carnaval"),
            Categoria(4, "Apadrinhamento", "Doação de brinquedos e roupas para crianças"),
        )

        @GetMapping
        fun getLista(
            @RequestParam(required = false) pesquisa: String?
        ): ResponseEntity<List<Categoria>> {
            if (pesquisa == null) {
                if (categorias.isEmpty()) {
                    return ResponseEntity.status(204).build()
                }
                return ResponseEntity.status(200).body(categorias)
            }

            val listaFiltrada = categorias.filter {
                it.nome!!.contains(pesquisa, ignoreCase = true) || it.descricao!!.contains(pesquisa, ignoreCase = true)
            }

            if (listaFiltrada.isEmpty()) {
                return ResponseEntity.status(204).build()
            }
            return ResponseEntity.status(200).body(listaFiltrada)
        }

        @PostMapping
        fun post(@RequestBody novaCategoria: Categoria): ResponseEntity<Categoria> {
            val novoId = if (categorias.isEmpty()) 1 else categorias.maxOf { it.id!! } + 1

            novaCategoria.id = novoId
            categorias.add(novaCategoria)

            return ResponseEntity.status(201).body(novaCategoria)
        }


        @DeleteMapping("/{id}")
        fun delete(@PathVariable id:Int): ResponseEntity<Void> {
            val idValido = categorias.count { it.id == id } > 0

            if (idValido) {
                categorias.removeIf { it.id == id }
                return ResponseEntity.status(204).build()
            }
            return ResponseEntity.status(404).build()
        }

        @GetMapping("/{id}")
        fun getUm(@PathVariable id:Int): ResponseEntity<Categoria> {
            val categoriaEncontrada = categorias.find { it.id == id }
            if (categoriaEncontrada == null) {
                return ResponseEntity.status(404).build()
            }

            return ResponseEntity.status(200).body(categoriaEncontrada)
        }


        @PutMapping("/{id}")
        fun put(@PathVariable id:Int, @RequestBody categoriaAtualizada: Categoria): ResponseEntity<Categoria> {
            val categoriaEncontrada = categorias.find { it.id == id }

            if (categoriaEncontrada == null) {
                return ResponseEntity.status(404).build()
            }

            val posicaoCategoriaEncontrada = categorias.indexOf(categoriaEncontrada)
            categoriaAtualizada.id = id
            categorias[posicaoCategoriaEncontrada] = categoriaAtualizada

            return ResponseEntity.status(200).body(categoriaAtualizada)
        }



}