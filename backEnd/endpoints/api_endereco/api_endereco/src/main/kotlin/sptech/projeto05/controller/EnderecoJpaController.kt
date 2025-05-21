package sptech.projeto05.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.projeto05.entity.Endereco
import sptech.projeto05.repository.EnderecoRepository

@RestController
@RequestMapping("/endereco")
class EnderecoJpaController(val repositorio: EnderecoRepository) {

    @PostMapping
    fun PostEndereco(@RequestBody @Valid endereco: Endereco): ResponseEntity<Endereco> {
        val novoEndereco = repositorio.save(endereco)
        return ResponseEntity.status(201).body(novoEndereco)
    }

    @GetMapping
    fun GetEndereco(): ResponseEntity<List<Endereco>> {
        val Endereco = repositorio.findAll()
        return if (Endereco.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(Endereco)
        }
    }

    @PutMapping("/{id_endereco}")
    fun PutEndereco(
        @PathVariable id_endereco: Int,
        @RequestBody enderecoAtualizado: Endereco
    ):
            ResponseEntity<Endereco> {
        if (!repositorio.existsById(id_endereco)) {
            return ResponseEntity.status(404).build()
        }
        enderecoAtualizado.id_endereco = id_endereco
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