package com.api.mocuti.controller

import com.api.mocuti.entity.NotaFeedback
import com.api.mocuti.repository.NotaFeedbackRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Notas de Feedback", description = "Operações relacionadas às notas atribuídas aos feedbacks")
@RequestMapping("/notas-feedbacks")
class NotaFeedbackJpaController(var repositorioNotaFeedback: NotaFeedbackRepository) {

    @Operation(
        summary = "Listar todas as notas de feedback",
        description = "Retorna todas as notas de feedback cadastradas no sistema"
    )
    @GetMapping
    fun get(): ResponseEntity<List<NotaFeedback>> {
        val notaFeedback = repositorioNotaFeedback.findAll()

        return if (notaFeedback.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(notaFeedback)
        }
    }
}