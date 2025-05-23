package com.api.mocuti.controller

import com.api.mocuti.entity.Categoria
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.api.mocuti.repository.CategoriaRepository

@RestController
@RequestMapping("/categorias")
class CategoriaJpaController(val repositorio: CategoriaRepository) {
    /*
    Definimos um construtor que recebe um objeto do tipo CategoriaRepository
    Assim, o Spring vai INJETAR um objeto desse tipo pronto para uso.
     */

    @GetMapping
    fun get(): ResponseEntity<List<Categoria>> {
        // repositorio.findAll() -> faz um "select * from musica"
        val categorias = repositorio.findAll()

        return if (categorias.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(categorias)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id:Int):
            ResponseEntity<Categoria> {
        /*
        findById() retorna um Optional da entidade
        "por baixo dos panos" executa um
        "select * from musica where id = ?"
         */
        val categoria = repositorio.findById(id)

        /*
ResponseEntity.of(<variável Optional>)
- se a variável Optional tiver valor:
  retorna 200 com o valor no corpo
- caso contrário, retorna 404 sem corpo
         */
        return ResponseEntity.of(categoria)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:Int):
            ResponseEntity<Void> {
        /*
        O existsById() verifica se o id indicado existe na tabela
        por baixo dos panos, ele faz um...
        "select count(*) from musica where id = ?"
         */
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
// por dos panos o deleteById faz um "delete from musica where id = ?"
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @PostMapping
    fun post(@RequestBody @Valid novaMusica: Categoria):
            ResponseEntity<Categoria> {
        /*
        save() pode fazer INSERT ou UPDATE
        Se o identificador for vazio, fará INSERT
        Caso contrário, o JPA verifica se existe na base.
        Se não existir, faz INSERT. Caso contrário faz update

        Seu retorno é a entidade salva
        Com todos os valores pós atualização preenchidos
         */
        val categoria = repositorio.save(novaMusica)
        return ResponseEntity.status(201).body(categoria)
    }

    /*
Crie o endpoint PUT /musicas/{id}  {JSON na requisição}
Se o id não existir, retorne 404 sem corpo
Caso contrário, atualize a música e retorne 200
com a música atualizada no corpo da resposta.
     */
    @PutMapping("/{id}")
    fun put(
        @PathVariable id: Int,
        @RequestBody categoriaAtualizada: Categoria
    ):
            ResponseEntity<Categoria> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }
        categoriaAtualizada.id = id
        val musica = repositorio.save(categoriaAtualizada)
        return ResponseEntity.status(200).body(musica)
    }


}