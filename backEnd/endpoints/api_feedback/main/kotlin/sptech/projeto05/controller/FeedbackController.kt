package sptech.projeto05.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.projeto05.entity.Feedback
import sptech.projeto05.repository.FeedbackRepository

@RestController
@RequestMapping("/feedback")
class FeedbackController(val repositorio: FeedbackRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Feedback>> {
        val feedback = repositorio.findAll()

        return if (feedback.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(feedback)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id:Int):
            ResponseEntity<Feedback> {
        val feedback = repositorio.findById(id)
        return ResponseEntity.of(feedback)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:Int):
            ResponseEntity<Void> {
        /*
        O existsById() verifica se o id indicado existe na tabela
        por baixo dos panos, ele faz um...
        "select count(*) from feedback where id = ?"
         */
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
        // por dos panos o deleteById faz um "delete from feeback where id = ?"
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @PostMapping
    fun post(@RequestBody @Valid novoFeedback: Feedback):
            ResponseEntity<Feedback> {
        /*
        save() pode fazer INSERT ou UPDATE
        Se o identificador for vazio, fará INSERT
        Caso contrário, o JPA verifica se existe na base.
        Se não existir, faz INSERT. Caso contrário faz update

        Seu retorno é a entidade salva
        Com todos os valores pós atualização preenchidos
         */
        val Feedback = repositorio.save(novoFeedback)
        return ResponseEntity.status(201).body(Feedback)
    }

    @PutMapping("/{id}")
    fun put(
        @PathVariable id: Int,
        @RequestBody feedbackAtualizado: Feedback
    ):
            ResponseEntity<Feedback> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }
        feedbackAtualizado.id = id
        val Feedback = repositorio.save(feedbackAtualizado)
        return ResponseEntity.status(200).body(Feedback)
    }

}